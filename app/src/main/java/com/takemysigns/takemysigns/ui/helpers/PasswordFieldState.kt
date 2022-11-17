package com.takemysigns.takemysigns.ui.helpers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class PasswordFieldState(label: String) : InputFieldState(label, validator = ::isPasswordValid) {
    override var text by mutableStateOf("password")
}

private fun isPasswordValid(password: String): Boolean {
    return password.isNotBlank() && password.length >= 8
}
