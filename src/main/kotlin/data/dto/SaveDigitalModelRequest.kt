
package com.ZeRo.dto

import kotlinx.serialization.Serializable

@Serializable
data class SaveDigitalModelRequest(
    val frontPhotoUrl: String,
    val sidePhotoUrl: String,
    val backPhotoUrl: String
)
