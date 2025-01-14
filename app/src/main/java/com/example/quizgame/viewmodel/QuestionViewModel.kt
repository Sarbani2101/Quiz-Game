package com.example.quizgame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizgame.apiwork.ApiService
import com.example.quizgame.apiwork.QuestionModel
import com.example.quizgame.apiwork.Result
import com.example.quizgame.apiwork.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _questionList = MutableLiveData<List<Result>>()
    val questionList: LiveData<List<Result>> get() = _questionList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchQuestions() {
        if (_questionList.value != null) {
            return
        }
        _isLoading.value = true
        val retrofitApi = UserService.createService(ApiService::class.java)
        val call: Call<QuestionModel> = retrofitApi.getObj()

        call.enqueue(object : Callback<QuestionModel> {
            override fun onResponse(call: Call<QuestionModel>, response: Response<QuestionModel>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _questionList.value = it.results
                    }
                } else {
                    _errorMessage.value = "Failed to fetch questions"
                }
            }

            override fun onFailure(call: Call<QuestionModel>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.localizedMessage
            }
        })
    }

}
