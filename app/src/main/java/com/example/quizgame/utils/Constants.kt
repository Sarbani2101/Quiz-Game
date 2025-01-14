package com.example.quizgame.utils


import android.content.Context
import android.content.SharedPreferences
import com.example.quizgame.apiwork.Result

object Constants {
    const val USER_NAME = "user_name"
    const val TOTAL_QUESTIONS = "total_questions"
    const val SCORE = "correct_answers"
    private const val PREF_NAME = "QuizGamePrefs"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    const val KEY_USER_EMAIL = "userEmail"

    fun getShuffled(result: Result): List<String> {
        val options = result.incorrect_answers.toMutableList()
        options.add(result.correct_answer)
        return options.shuffled()
    }

    fun saveUserLoginStatus(context: Context, isLoggedIn: Boolean, email: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.putString(KEY_USER_EMAIL, email)
        editor.apply()
    }

    fun getUserLoginStatus(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserEmail(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    fun logoutUser(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}



