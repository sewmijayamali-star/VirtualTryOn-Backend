
package com.ZeRo.routes

import com.ZeRo.service.ProductService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productRoutes() {

    val service = ProductService()

    route("/api/products") {

        // Get all products
        get {
            try {
                val products = service.getAllProducts()

                call.respond(
                    HttpStatusCode.OK,
                    products
                )

            } catch (e: Exception) {

                call.application.log.error(
                    "Failed to retrieve products",
                    e
                )

                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("message" to "Could not retrieve products")
                )
            }
        }

        // Get product by ID
        get("/{id}") {

            val productId = call.parameters["id"]?.toIntOrNull()

            if (productId == null || productId <= 0) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "Invalid product ID")
                )
                return@get
            }

            try {
                val product = service.getProductById(productId)

                if (product == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("message" to "Product not found")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        product
                    )
                }

            } catch (e: Exception) {

                call.application.log.error(
                    "Failed to retrieve product",
                    e
                )

                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("message" to "Could not retrieve product")
                )
            }
        }
    }
}
