package com.takemysigns.takemysigns.ui.helpers

import android.util.Patterns

class EmailFieldState(label: String) : InputFieldState(label, validator = ::isEmailValid) {
}

private fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}