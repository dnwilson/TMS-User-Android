package com.takemysigns.takemysigns.models

import com.squareup.moshi.Json

data class AppButton(
    override val title: String,
    override val icon: String?,
    override val script: String?,
    override val url: String?,
    @Json(ignore = true)
    override var isGet: () -> Boolean = { url?.isNotBlank() == true && script?.isBlank() == true },
    @Json(ignore = true)
    override var isScript: () -> Boolean = { url.isNullOrBlank() || script?.isNotBlank() ?: false }
): FyreButton {

}

interface FyreButton {
    val title: String
    val icon: String?
    val script: String?
    val url: String?
    val isGet: () -> Boolean
    val isScript: () -> Boolean
}