package com.example.quizgame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizgame.apiwork.ApiService
import com.example.quizgame.apiwork.RegisterData
import com.example.quizgame.apiwork.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun registerUser(fullName: String, email: String, phone: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _error.postValue("Passwords do not match")
            return
        }

        _isLoading.postValue(true)

        val apiService = UserService.buildService(ApiService::class.java)
        val call = apiService.registerUser(fullName, email, phone, password, confirmPassword)

        call.enqueue(object : Callback<RegisterData> {
            override fun onResponse(call: Call<RegisterData>, response: Response<RegisterData>) {
                _isLoading.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    _registrationStatus.postValue(true)
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterData>, t: Throwable) {
                _isLoading.postValue(false)
                _error.postValue("Network error: ${t.message}")
            }
        })
    }
}
