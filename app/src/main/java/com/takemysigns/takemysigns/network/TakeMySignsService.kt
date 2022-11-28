package com.takemysigns.takemysigns.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.takemysigns.takemysigns.models.LoginResponse
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
