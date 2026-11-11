
package service

import data.dto.fashn.FashnRequest
import data.dto.fashn.FashnResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import data.dto.fashn.FashnStatusResponse


object FashnService {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    private const val API_URL = "https://api.fashn.ai/v1/run"
    private const val STATUS_URL = "https://api.fashn.ai/v1/status"

    suspend fun generateTryOn(
        modelImage: String,
        productImage: String
    ): FashnResponse {

        val apiKey = System.getenv("FASHN_API_KEY")
            ?: throw IllegalStateException("FASHN_API_KEY is not configured")

        val request = FashnRequest(
            inputs = data.dto.fashn.FashnInputs(
                modelImage = modelImage,
                productImage = productImage
            )
        )

        val response = client.post(API_URL) {
            header("Authorization", "Bearer $apiKey")
            contentType(ContentType.Application.Json)
            setBody(request)
        }


        val responseText = response.bodyAsText()

        if (response.status.value !in 200..299) {
            throw Exception("FASHN API error: ${response.status.value} - $responseText")
        }

        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString<FashnResponse>(responseText)
    }
    suspend fun checkTryOnStatus(
        predictionId: String
    ): FashnStatusResponse {

        val apiKey = System.getenv("FASHN_API_KEY")
            ?: throw IllegalStateException("FASHN_API_KEY is not configured")

        val response = client.get("$STATUS_URL/$predictionId") {
            header("Authorization", "Bearer $apiKey")
        }

        val responseText = response.bodyAsText()

        if (response.status.value !in 200..299) {
            throw Exception(
                "FASHN Status API error: ${response.status.value} - $responseText"
            )
        }

        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString<FashnStatusResponse>(responseText)
    }


}

