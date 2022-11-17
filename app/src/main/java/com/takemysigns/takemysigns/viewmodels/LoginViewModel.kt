package com.takemysigns.takemysigns.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.network.TakeMySignsApiProvider
import com.takemysigns.takemysigns.network.TakeMySignsRepository
import com.takemysigns.takemysigns.network.User
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginViewModel(private val repo: TakeMySignsRepository) : ViewModel() {
    private val TAG: String = LoginViewModel::class.java.name

    val errorMessage = MutableLiveData<String>()
    var user = MutableLiveData<User?>()
    var job: Job? = null
    var token = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun signIn(phoneNumber: String, password: String) {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO).launch {
            val params = JSONObject()
            params.put("phone_number", phoneNumber)
            params.put("password", password)

            Log.d(TAG, "Making api call...")
            val paramString = params.toString()
            val requestBody = paramString.toRequestBody("application/json".toMediaTypeOrNull())
            val response = repo.signIn(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    user.value = loginResponse!!.user
                    token.value = loginResponse.token
                    TakeMySignsApp.setAuthToken(loginResponse.token.toString())
                } else {
                    onError("Error : ${response.message()} ")
                }
                loading.value = false
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}


@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory constructor(private val repository: TakeMySignsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(this.repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}