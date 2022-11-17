package com.takemysigns.takemysigns.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TakeMySignsApp : Application() {
    override fun onCreate() {
        super.onCreate()

//        val context = applicationContext
        Log.d("TakeMySignsApp", "onCreateTriggered...")
        val masterKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            applicationContext,
            "TakeMySignsPrefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        editor = sharedPreferences.edit()
        instance = this
    }

    companion object {
        private var instance : TakeMySignsApp? = null
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var editor: Editor

//        private fun applicationContext(): Context {
//            return instance!!.applicationContext
//        }

        fun getSharedPreference(): SharedPreferences? {
            return sharedPreferences
        }

        fun getInstance(context: Context) : TakeMySignsApp {
            if (instance == null) { instance = TakeMySignsApp() }

            return instance!!
        }

        fun getAuthToken() : String? {
            return sharedPreferences.getString("KEY_AUTH_TOKEN", null)
        }

        fun setAuthToken(token: String) {
            editor.putString("KEY_AUTH_TOKEN", token).apply()
        }

        fun getPushToken() : String? {
            return sharedPreferences.getString("KEY_PUSH_TOKEN", null)
        }

        fun setPushToken(token: String) {
            editor.putString("KEY_PUSH_TOKEN", token)?.apply()
        }

        fun isFirstRun() : Boolean {
            return sharedPreferences.getBoolean("KEY_FIRST_RUN", true)
        }

        fun disableFirstRun() {
            return editor.putBoolean("KEY_FIRST_RUN", false).apply()
        }

        fun resetFirstRun() {
            return editor.putBoolean("KEY_FIRST_RUN", true).apply()
        }
    }
}