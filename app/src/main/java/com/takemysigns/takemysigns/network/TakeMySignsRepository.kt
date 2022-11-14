package com.takemysigns.takemysigns.network

import android.content.SharedPreferences
import com.takemysigns.takemysigns.util.CryptoManager
import java.io.File
import java.io.FileOutputStream

object TakeMySignsRepository {
//    private var tmsApi = TakeMySignsApi? = null
    fun signIn(phoneNumber: String, password: String) {
        TakeMySignsApiProvider.login(phoneNumber, password)

        println("Login successfull")
    }
}