package com.takemysigns.takemysigns.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TakeMySignsApp : Application() {
    companion object {
        private var instance : TakeMySignsApp? = null
        private var masterKey: MasterKey = MasterKey.Builder(applicationContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            applicationContext(),
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        private var editor = sharedPreferences.edit()


        private fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun getSharedPreference(): SharedPreferences? {
            return sharedPreferences
        }

        fun getInstance() : TakeMySignsApp {
            if (instance == null) { instance = TakeMySignsApp() }

            return instance!!
        }

        fun getAuthToken() : String? {
            return sharedPreferences.getString("KEY_AUTH_TOKEN", null)
        }

        fun setAuthToken(token: String) {
            editor.putString("KEY_AUTH_TOKEN", token)
        }
    }
//
//    var authToken: String?
//        get() = pref.getString("KEY_TOKEN", null)
//        set(value) = pref.edit { putString("KEY_TOKEN", value) }
//
//    var pushToken: Boolean
//        get() = pref.getBoolean("KEY_PUSH_TOKEN", false)
//        set(value) = pref.edit { putBoolean("KEY_PUSH_TOKEN", value) }
//
//    private val pref = context.getSharedPreferences("TAKE_MY_SIGNS", Context.MODE_PRIVATE)
//    val cyptoManager = CryptoManager()
}