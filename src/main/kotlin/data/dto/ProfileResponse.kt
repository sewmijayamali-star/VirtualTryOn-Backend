package com.ZeRo.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val message: String,
    val userId: Int,
    val email: String
)