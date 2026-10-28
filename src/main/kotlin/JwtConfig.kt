package com.ZeRo

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {

    private const val issuer = "com.Zero.vestitry"
    private const val audience = "vestitry-users"

    private val secret: String
        get() = System.getenv("JWT_SECRET")
            ?: error("JWT_SECRET environment variable is missing")

    fun generateToken(userId: Int, email: String): String {

        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withExpiresAt(
                Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L)
            )
            .sign(Algorithm.HMAC256(secret))
    }
}