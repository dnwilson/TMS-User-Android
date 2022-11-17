package com.takemysigns.takemysigns.network

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response

class TakeMySignsRepository constructor(private val tmsService: TakeMySignsService) {
    private val TAG: String = TakeMySignsRepository::class.java.name
    suspend fun signIn(requestBody: RequestBody) = tmsService.login(requestBody)
}