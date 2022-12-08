package com.takemysigns.takemysigns.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class TakeMySignsApp : Application() {
    override fun onCreate() {
        super.onCreate()

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
        const val REQUESTED_PAGE = "requested_page"
        const val NOTIFICATION_ID = "82416"
        private var instance : TakeMySignsApp? = null
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var editor: Editor
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

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

        fun setPushTokenSaved(saved: Boolean = false) {
            editor.putBoolean("KEY_PUSH_TOKEN_SAVED", saved).apply()
        }

        fun pushTokenSaved() : Boolean {
            return sharedPreferences.getBoolean("KEY_PUSH_TOKEN_SAVED", false)
        }

        fun disableFirstRun() {
            return editor.putBoolean("KEY_FIRST_RUN", false).apply()
        }

        fun isLoggedIn(): Boolean {
            return getAuthToken() != null
        }

        fun resetFirstRun() {
            return editor.putBoolean("KEY_FIRST_RUN", true).apply()
        }
    }
}