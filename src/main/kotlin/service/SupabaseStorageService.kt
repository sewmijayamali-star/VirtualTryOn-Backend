
package com.ZeRo.service

import java.net.HttpURLConnection
import java.net.URI

object SupabaseStorageService {

    private val supabaseUrl: String
        get() = System.getenv("SUPABASE_URL")
            ?: error("SUPABASE_URL is missing")

    private val secretKey: String
        get() = System.getenv("SUPABASE_SERVICE_ROLE_KEY")
            ?: error("SUPABASE_SERVICE_ROLE_KEY is missing")

    fun uploadImage(
        userId: Int,
        fileName: String,
        bytes: ByteArray,
        contentType: String
    ): String {
        require(userId > 0) { "Invalid user ID" }
        require(bytes.isNotEmpty()) { "Image is empty" }
        require(bytes.size <= 10 * 1024 * 1024) {
            "Image exceeds the 10 MB limit"
        }
        require(
            contentType == "image/jpeg" ||
                    contentType == "image/png" ||
                    contentType == "image/webp"
        ) { "Unsupported image type" }

        val safeName = fileName
            .substringAfterLast('/')
            .substringAfterLast('\\')
            .replace(Regex("[^a-zA-Z0-9._-]"), "_")

        val path = "$userId/${java.util.UUID.randomUUID()}-$safeName"
        val baseUrl = supabaseUrl.trimEnd('/')

        val connection = URI.create(
            "$baseUrl/storage/v1/object/digital-model-photos/$path"
        ).toURL().openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.connectTimeout = 15000
            connection.readTimeout = 30000
            connection.setRequestProperty("apikey", secretKey)
            connection.setRequestProperty(
                "Authorization", "Bearer $secretKey"
            )
            connection.setRequestProperty("Content-Type", contentType)
            connection.setRequestProperty("x-upsert", "false")
            connection.setFixedLengthStreamingMode(bytes.size)

            connection.outputStream.use { output ->
                output.write(bytes)
            }

            val status = connection.responseCode
            if (status !in 200..299) {
                val errorBody = connection.errorStream
                    ?.bufferedReader()
                    ?.use { it.readText() }
                    .orEmpty()

                throw IllegalStateException(
                    "Supabase upload failed ($status): $errorBody"
                )
            }

            // Private bucket: save the storage path, not a public URL.
            return path
        } finally {
            connection.disconnect()
        }
    }
}
