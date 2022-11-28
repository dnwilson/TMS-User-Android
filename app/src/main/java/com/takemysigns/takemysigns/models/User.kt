package com.takemysigns.takemysigns.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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