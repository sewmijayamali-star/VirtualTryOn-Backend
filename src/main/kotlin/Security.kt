package com.ZeRo

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {

    val secret = System.getenv("JWT_SECRET")
        ?: error("JWT_SECRET environment variable is missing")

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "VestiTry"

            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("com.Zero.vestitry")
                    .withAudience("vestitry-users")
                    .build()
            )

            validate { credential ->
                val userId = credential.payload
                    .getClaim("userId")
                    .asInt()

                if (userId != null && userId > 0) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}