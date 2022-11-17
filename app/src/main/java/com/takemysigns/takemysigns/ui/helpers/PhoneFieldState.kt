package com.takemysigns.takemysigns.ui.helpers

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class PhoneFieldState(label: String) : InputFieldState(label, validator = ::isPhoneNumberValid) {
    override var text by mutableStateOf("1111111111")
}

private fun isPhoneNumberValid(phoneNumber: String): Boolean {
    return phoneNumber.isNotBlank() && Patterns.PHONE.matcher(phoneNumber).matches()
}
