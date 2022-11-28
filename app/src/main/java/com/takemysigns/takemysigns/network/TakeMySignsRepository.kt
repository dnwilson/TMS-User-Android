package com.takemysigns.takemysigns.network

import okhttp3.RequestBody

class TakeMySignsRepository constructor(private val tmsService: TakeMySignsService) {
    private val TAG: String = TakeMySignsRepository::class.java.name
    suspend fun signIn(requestBody: RequestBody) = tmsService.login(requestBody)
}