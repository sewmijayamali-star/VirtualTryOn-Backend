package data.dto.fashn

import kotlinx.serialization.Serializable

@Serializable
data class FashnStatusResponse(
    val id: String? = null,
    val status: String? = null,
    val output: List<String>? = null,
    val error: String? = null
)