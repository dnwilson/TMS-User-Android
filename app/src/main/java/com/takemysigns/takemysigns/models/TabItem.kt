package com.takemysigns.takemysigns.models

import com.squareup.moshi.JsonClass
import com.takemysigns.takemysigns.R

@JsonClass(generateAdapter = true)
data class TabItem(
    val label: String,
    val icon: String,
    val url: String,
) {
    fun image(): Int {
        return R.drawable::class.java.getId("R.drawable.$icon")
    }
}

inline fun <reified T: Class<*>> T.getId(resourceName: String): Int {
    return try {
        val idField = getDeclaredField (resourceName)
        idField.getInt(idField)
    } catch (e:Exception) {
        e.printStackTrace()
        -1
    }
}