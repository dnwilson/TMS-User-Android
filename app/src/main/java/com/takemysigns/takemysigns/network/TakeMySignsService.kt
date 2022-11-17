package com.takemysigns.takemysigns.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.takemysigns.takemysigns.util.BASE_URL
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TakeMySignsService {
    @Headers("Content-Type: application/json")
    @POST("/api/login")
    suspend fun login(@Body requestBody: RequestBody): Response<LoginResponse>

    companion object {
        var tmsService: TakeMySignsService? = null
        fun getInstance() : TakeMySignsService {
            if (tmsService == null) {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL) // change this IP for testing by your actual machine IP
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
                tmsService = retrofit.create(TakeMySignsService::class.java)
            }
            return tmsService!!
        }

    }
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