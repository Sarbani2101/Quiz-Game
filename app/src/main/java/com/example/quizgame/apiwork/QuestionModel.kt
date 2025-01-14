package com.example.quizgame.apiwork

data class QuestionModel(
    val response_code: Int,
    val results: List<Result>
)

data class Result(
    val category: String,
    val difficulty: String,
    val question: String,
    val type: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
