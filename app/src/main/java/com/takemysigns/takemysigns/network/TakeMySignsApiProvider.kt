package com.takemysigns.takemysigns.network

import android.util.Base64
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.util.BASE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object TakeMySignsApiProvider {
    private val TAG: String = TakeMySignsApiProvider::class.java.name

    fun login(phoneNumber: String, password: String): Pair<User, String> {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // change this IP for testing by your actual machine IP
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val service = retrofit.create(TakeMySignsApi::class.java)
        val params = JSONObject()
        params.put("phone_number", phoneNumber)
        params.put("password", password)

        val paramString = params.toString()
        val requestBody = paramString.toRequestBody("application/json".toMediaTypeOrNull())
        var user: User? = null
        var token: String? = null

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.login(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    user = loginResponse!!.user
                    token = loginResponse.token

                    token?.let { TakeMySignsApp.setAuthToken(it) }

//                    val bytes = token.encodeToByteArray()
//                    val file = File(LocalContext.current.filesDir, "takemysigns.txt")
//                    if (!file.exists()) { file.createNewFile() }
//
//                    val fos = FileOutputStream(file)
//                    CryptoManager().encrypt(bytes = bytes!!, outputStream = fos)
                } else {
                    Log.e(TAG, "Login failed")
                }
            }
        }

        return user!! to token!!
    }

    fun get(phoneNumber: String, password: String) : TakeMySignsApi {
        val credentials = ("$phoneNumber:$password").toByteArray()
        val authToken = "Basic"+ Base64.encodeToString(credentials, Base64.NO_WRAP)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Authorization", authToken)
                val request = builder.build()
                chain.proceed(request)
            }
            .build()

        // create retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(TakeMySignsApi::class.java)
    }
}