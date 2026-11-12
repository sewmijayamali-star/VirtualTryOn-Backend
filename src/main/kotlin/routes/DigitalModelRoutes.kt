package com.ZeRo.routes

import com.ZeRo.dto.DigitalModelResponse
import com.ZeRo.service.DigitalModelService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.content.*

fun Route.digitalModelRoutes() {

    val service = DigitalModelService()

    authenticate("auth-jwt") {
        route("/api/digital-model") {

            // Upload one model photo
            post("/photos") {

                val principal = call.principal<JWTPrincipal>()
                val userId = principal
                    ?.payload
                    ?.getClaim("userId")
                    ?.asInt()

                if (userId == null || userId <= 0) {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        mapOf("message" to "Invalid user token")
                    )
                    return@post
                }

                var modelBytes: ByteArray? = null
                var modelType: String? = null

                val multipart = call.receiveMultipart()

                multipart.forEachPart { part ->

                    if (part is PartData.FileItem && part.name == "modelPhoto") {

                        modelType = part.contentType?.toString()

                        modelBytes = part.provider().use {
                            it.readBytes()
                        }
                    }

                    part.release()
                }

                val uploadedModelBytes = modelBytes
                val uploadedModelType = modelType

                if (uploadedModelBytes == null || uploadedModelBytes.isEmpty()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("message" to "Model photo is required")
                    )
                    return@post
                }

                if (uploadedModelType?.startsWith("image/") != true) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("message" to "Please upload a valid image")
                    )
                    return@post
                }

                try {

                    val result = service.saveDigitalModel(
                        userId = userId,
                        modelBytes = uploadedModelBytes,
                        modelType = uploadedModelType
                    )

                    call.respond(
                        HttpStatusCode.OK,
                        DigitalModelResponse(
                            message = "Digital model photo saved",
                            modelId = result.modelId,
                            userId = result.userId,
                            frontPhotoUrl = result.frontPhotoUrl,
                            sidePhotoUrl = result.sidePhotoUrl,
                            backPhotoUrl = result.backPhotoUrl,
                            status = result.status
                        )
                    )

                } catch (e: Exception) {

                    call.application.log.error(
                        "Digital model photo upload failed",
                        e
                    )

                    call.respond(
                        HttpStatusCode.InternalServerError,
                        mapOf("message" to "Could not save digital model photo")
                    )
                }
            }

            // Get saved model photo
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal
                    ?.payload
                    ?.getClaim("userId")
                    ?.asInt()

                if (userId == null || userId <= 0) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }

                val model = service.getDigitalModel(userId)

                if (model == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("message" to "Digital model not found")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        DigitalModelResponse(
                            message = "Digital model retrieved",
                            modelId = model.modelId,
                            userId = model.userId,
                            modelImageUrl = model.modelImageUrl,
                            status = model.status
                        )
                    )
                }
            }
        }
    }
}