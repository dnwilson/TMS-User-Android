package com.takemysigns.takemysigns.ui.helpers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class TextFieldState(
    private val validator: (String) -> Boolean = { true },
    val label: String
) {
//    constructor(validator: (String) -> Boolean = { true }, label: String, errorString: String) : this()

    var text by mutableStateOf("")
    var error by mutableStateOf<String?>(null)

    fun validate() {
        error = if (validator(text)) null else errorMessage()
    }

    private fun errorMessage() = "$label is invalid"
}