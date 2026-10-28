
package com.ZeRo.dto

import kotlinx.serialization.Serializable

@Serializable
data class DigitalModelResponse(
    val message: String,
    val modelId: Int,
    val userId: Int,
    val frontPhotoUrl: String,
    val sidePhotoUrl: String,
    val backPhotoUrl: String,
    val status: String
)
