package com.takemysigns.takemysigns.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.takemysigns.takemysigns.network.TakeMySignsRepository

class LoginViewModel(private val tmsRepo: TakeMySignsRepository) : ViewModel() {
    fun signIn(phoneNumber: String, password: String) {
        tmsRepo.signIn(phoneNumber, password)
    }
}


@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(tmsRepo = TakeMySignsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}