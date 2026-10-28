package com.ZeRo.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val userId: Int,
    val token: String
)