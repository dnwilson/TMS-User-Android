package com.takemysigns.takemysigns.ui.helpers

class PasswordFieldState(label: String) : InputFieldState(label, validator = ::isPasswordValid)

private fun isPasswordValid(password: String): Boolean {
    return password.isNotBlank() && password.length >= 8
}
