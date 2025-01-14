package com.example.quizgame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizgame.apiwork.ApiService
import com.example.quizgame.apiwork.LoginData
import com.example.quizgame.apiwork.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _userDetails = MutableLiveData<LoginData>()
    val userDetails: LiveData<LoginData> get() = _userDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loginUser(email: String, password: String) {
        _isLoading.postValue(true)

        val apiService = UserService.buildService(ApiService::class.java)
        apiService.loginUser(email, password).enqueue(object : Callback<LoginData> {
            override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                _isLoading.postValue(false)

                if (response.isSuccessful) {
                    _loginSuccess.postValue(true)
                    _userDetails.postValue(response.body())
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(extractErrorMessage(errorBody))
                }
            }

            override fun onFailure(call: Call<LoginData>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Network Error: ${t.message}")
            }
        })
    }

    private fun extractErrorMessage(errorBody: String?): String {
        return if (errorBody != null && errorBody.contains("string")) {
            "Incorrect email or password"
        } else {
            "Login failed. Please try again."
        }
    }
}
