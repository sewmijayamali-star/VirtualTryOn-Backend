
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

                var front: ByteArray? = null
                var side: ByteArray? = null
                var back: ByteArray? = null

                var frontType: String? = null
                var sideType: String? = null
                var backType: String? = null

                val multipart = call.receiveMultipart(
                    formFieldLimit = 10 * 1024 * 1024
                )

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val type = part.contentType?.toString()
                        val bytes = part.streamProvider().use {
                            it.readBytes()
                        }

                        when (part.name) {
                            "frontPhoto" -> {
                                front = bytes
                                frontType = type
                            }
                            "sidePhoto" -> {
                                side = bytes
                                sideType = type
                            }
                            "backPhoto" -> {
                                back = bytes
                                backType = type
                            }
                        }
                    }
                    part.dispose()
                }

                if (front == null || side == null || back == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf(
                            "message" to
                                    "Front, side and back photos are required"
                        )
                    )
                    return@post
                }

                try {
                    val result = service.saveDigitalModel(
                        userId = userId,
                        frontBytes = front!!,
                        frontType = frontType ?: "",
                        sideBytes = side!!,
                        sideType = sideType ?: "",
                        backBytes = back!!,
                        backType = backType ?: ""
                    )

                    call.respond(
                        HttpStatusCode.OK,
                        DigitalModelResponse(
                            message = "Digital model photos saved",
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
                        mapOf("message" to "Could not save digital model photos")
                    )
                }
            }

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
                            frontPhotoUrl = model.frontPhotoUrl,
                            sidePhotoUrl = model.sidePhotoUrl,
                            backPhotoUrl = model.backPhotoUrl,
                            status = model.status
                        )
                    )
                }
            }
        }
    }
}
