package com.takemysigns.takemysigns.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TakeMySignsApi {
    @Headers("Content-Type: application/json")
    @POST("/api/login")
    suspend fun login(@Body requestBody: RequestBody): Response<LoginResponse>
}

@JsonClass(generateAdapter = true)
data class User(
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "code") val code: String?,
    @field:Json(name = "avatar") val avatar: String?,
    @field:Json(name = "first_name") val first_name: String?,
    @field:Json(name = "last_name") val last_name: String?,
    @field:Json(name = "dre_number") val dre_number: String?,
    @field:Json(name = "phone_number") val phone_number: String?,
    @field:Json(name = "password") val password: String?
) {
}

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @field:Json(name = "token") val token: String?,
    @field:Json(name = "user") val user: User?,
) {

}