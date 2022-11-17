package com.takemysigns.takemysigns.ui.helpers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


abstract class InputFieldState(
    var label: String,
    private val validator: (String) -> Boolean = { true }
) {
    open var text by mutableStateOf("")
    var error by mutableStateOf<String?>(null)

    fun validate() {
        error = if (isValid()) null else errorMessage()
    }

    fun isValid() = validator(text)

    private fun errorMessage() = "$label is invalid"
}