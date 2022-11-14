package com.takemysigns.takemysigns.ui.helpers

import android.util.Patterns

class PhoneFieldState(label: String) : InputFieldState(label, validator = ::isPhoneNumberValid) {
}

private fun isPhoneNumberValid(phoneNumber: String): Boolean {
    return phoneNumber.isNotBlank() && Patterns.PHONE.matcher(phoneNumber).matches()
}

//private fun phoneNumberErrorMessage(phoneNumber: String) = "Phone number is invalid"