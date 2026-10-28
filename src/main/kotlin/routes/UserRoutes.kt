package com.ZeRo.routes

import com.ZeRo.data.dto.RegisterRequest
import com.ZeRo.data.repository.UserRepository
import com.ZeRo.service.UserService
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import com.ZeRo.data.dto.RegisterResponse
import com.ZeRo.service.PasswordHasher
import com.ZeRo.data.dto.LoginRequest
import com.ZeRo.data.dto.LoginResponse
import io.ktor.http.HttpStatusCode
import com.ZeRo.JwtConfig

import com.ZeRo.data.dto.ProfileResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.get

fun Route.userRoutes() {

    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    route("/api/users") {

        post("/register") {

            val request = call.receive<RegisterRequest>()

            val userId = userService.createUser(
                name = request.name,
                email = request.email,
                passwordHash = PasswordHasher.hash(request.password)
            )

            call.respond(
                RegisterResponse(
                    message = "User registered successfully",
                    userId = userId
                )
            )
        }

        post("/login") {

            val request = call.receive<LoginRequest>()

            val user = userService.findUserByEmail(request.email)

            if (user == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("message" to "User not found")
                )
                return@post
            }

            val (userId, name, passwordHash) = user

            if (!PasswordHasher.verify(request.password, passwordHash)) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("message" to "Invalid email or password")
                )
                return@post
            }

            call.respond(
                LoginResponse(
                    message = "Login successful",
                    userId = userId,
                    token = JwtConfig.generateToken(
                        userId = userId,
                        email = request.email
                    )
                )
            )

        }

        authenticate("auth-jwt") {

            get("/me") {

                val principal = call.principal<JWTPrincipal>()

                val userId = principal!!
                    .payload
                    .getClaim("userId")
                    .asInt()

                val email = principal.payload
                    .getClaim("email")
                    .asString()

                call.respond(
                    ProfileResponse(
                        message = "Authenticated successfully",
                        userId = userId,
                        email = email
                    )
                )
            }
        }

    }
}