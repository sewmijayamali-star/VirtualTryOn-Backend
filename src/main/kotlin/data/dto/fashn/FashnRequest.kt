
package data.dto.fashn

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FashnRequest(
    @SerialName("model_name")
    val modelName: String = "tryon-max",

    @SerialName("inputs")
    val inputs: FashnInputs
)

@Serializable
data class FashnInputs(
    @SerialName("model_image")
    val modelImage: String,

    @SerialName("product_image")
    val productImage: String
)
