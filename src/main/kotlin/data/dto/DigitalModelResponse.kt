package com.ZeRo.dto

import kotlinx.serialization.Serializable

@Serializable
data class DigitalModelResponse(
    val message: String,
    val modelId: Int,
    val userId: Int,
    val modelImageUrl: String,
    val status: String
)