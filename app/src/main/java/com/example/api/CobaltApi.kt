package com.example.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class CobaltRequest(
    val url: String,
    val videoQuality: String = "720",
    val downloadMode: String = "auto",
    val audioFormat: String = "mp3",
    val filenamePattern: String = "classic"
)

data class PickerItem(
    val url: String?,
    val type: String?,
    val quality: String?
)

data class CobaltResponse(
    val status: String?, // "success", "error", "stream", "redirect", "picker"
    val url: String?,
    val text: String?,
    val picker: List<PickerItem>?
)

interface CobaltApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/")
    suspend fun downloadMedia(@Body request: CobaltRequest): CobaltResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.cobalt.tools/" // fallback URL

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun createService(): CobaltApiService {
        return retrofit.create(CobaltApiService::class.java)
    }
}
