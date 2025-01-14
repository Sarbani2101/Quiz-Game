package com.example.quizgame.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.quizgame.R
import com.example.quizgame.utils.Constants
import com.example.quizgame.viewmodel.QuestionViewModel
import com.example.quizgame.apiwork.Result

class QuestionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var progressBar: ProgressBar
    private lateinit var textViewProgress: TextView
    private lateinit var textViewQuestion: TextView
    private lateinit var textViewOptionOne: TextView
    private lateinit var textViewOptionTwo: TextView
    private lateinit var textViewOptionThree: TextView
    private lateinit var textViewOptionFour: TextView
    private lateinit var checkButton: Button
    private lateinit var progressLoading: ProgressBar
    private var email: String? = null

    private lateinit var name: String
    private var answered = false
    private var score = 0
    private var selectedAnswer = -1
    private var currentQuestionIndex = 0

    private val questionViewModel: QuestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("QuestionActivity", "onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        initViews()
        setListeners()

        name = intent.getStringExtra(Constants.USER_NAME) ?: "Unknown User"
        email = intent.getStringExtra(Constants.KEY_USER_EMAIL)

        questionViewModel.isLoading.observe(this, Observer { isLoading ->
            Log.d("QuestionActivity", "Loading state: $isLoading")
            showLoading(isLoading)
        })

        questionViewModel.questionList.observe(this, Observer { questions ->
            if (questions.isNotEmpty()) {
                displayQuestions(questions)
            } else {
                Toast.makeText(this, "No questions available.", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        questionViewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        questionViewModel.fetchQuestions()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        progressLoading = findViewById(R.id.progressLoading)
        textViewProgress = findViewById(R.id.text_view_progress)
        textViewQuestion = findViewById(R.id.txt)
        textViewOptionOne = findViewById(R.id.text_view_option_one)
        textViewOptionTwo = findViewById(R.id.text_view_option_two)
        textViewOptionThree = findViewById(R.id.text_view_option_three)
        textViewOptionFour = findViewById(R.id.text_view_option_four)
        checkButton = findViewById(R.id.button_check)
    }

    private fun setListeners() {
        textViewOptionOne.setOnClickListener(this)
        textViewOptionTwo.setOnClickListener(this)
        textViewOptionThree.setOnClickListener(this)
        textViewOptionFour.setOnClickListener(this)
        checkButton.setOnClickListener(this)
    }

    private fun showLoading(isLoading: Boolean) {
        progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun displayQuestions(questionsList: List<Result>) {
        val currentQuestion = questionsList.getOrNull(currentQuestionIndex)
        currentQuestion?.let {
            resetOptions()
            val shuffledOptions = Constants.getShuffled(it)

            progressBar.progress = currentQuestionIndex + 1
            textViewProgress.text = "${currentQuestionIndex + 1}/${questionsList.size}"
            textViewQuestion.text = it.question
            textViewOptionOne.text = shuffledOptions[0]
            textViewOptionTwo.text = shuffledOptions[1]
            textViewOptionThree.text = shuffledOptions[2]
            textViewOptionFour.text = shuffledOptions[3]

            checkButton.text = if (answered) "NEXT" else "CHECK"
        }
    }

    private fun resetOptions() {
        val options = listOf(textViewOptionOne, textViewOptionTwo, textViewOptionThree, textViewOptionFour)
        options.forEach { option ->
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.text_view_option_one -> selectOption(textViewOptionOne, 1)
            R.id.text_view_option_two -> selectOption(textViewOptionTwo, 2)
            R.id.text_view_option_three -> selectOption(textViewOptionThree, 3)
            R.id.text_view_option_four -> selectOption(textViewOptionFour, 4)
            R.id.button_check -> {
                if (answered) {
                    goToNextQuestion()
                } else {
                    checkAnswer()
                }
            }
        }
    }

    private fun selectOption(textView: TextView, optionNumber: Int) {
        if (answered) return
        resetOptions()
        selectedAnswer = optionNumber
        textView.setTextColor(Color.parseColor("#363A43"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background = ContextCompat.getDrawable(this, R.drawable.select_border)
    }

    private fun checkAnswer() {
        val currentQuestion = questionViewModel.questionList.value?.get(currentQuestionIndex)
        val correctAnswerIndex = currentQuestion?.let { Constants.getShuffled(it).indexOf(it.correct_answer) } ?: -1
        if (selectedAnswer - 1 == correctAnswerIndex) {
            score++
            highlightAnswer(selectedAnswer)
        } else {
            highlightWrongAnswer(selectedAnswer)
            highlightAnswer(correctAnswerIndex + 1)
        }
        answered = true
        checkButton.text = "NEXT"
    }

    private fun highlightAnswer(answer: Int) {
        when (answer) {
            1 -> textViewOptionOne.background = ContextCompat.getDrawable(this, R.drawable.right_border)
            2 -> textViewOptionTwo.background = ContextCompat.getDrawable(this, R.drawable.right_border)
            3 -> textViewOptionThree.background = ContextCompat.getDrawable(this, R.drawable.right_border)
            4 -> textViewOptionFour.background = ContextCompat.getDrawable(this, R.drawable.right_border)
        }
    }

    private fun highlightWrongAnswer(answer: Int) {
        when (answer) {
            1 -> textViewOptionOne.background = ContextCompat.getDrawable(this, R.drawable.wrong_border)
            2 -> textViewOptionTwo.background = ContextCompat.getDrawable(this, R.drawable.wrong_border)
            3 -> textViewOptionThree.background = ContextCompat.getDrawable(this, R.drawable.wrong_border)
            4 -> textViewOptionFour.background = ContextCompat.getDrawable(this, R.drawable.wrong_border)
        }
    }

    private fun goToNextQuestion() {
        if (currentQuestionIndex < questionViewModel.questionList.value?.size?.minus(1) ?: 0) {
            currentQuestionIndex++
            answered = false
            displayQuestions(questionViewModel.questionList.value ?: emptyList())
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(Constants.USER_NAME, name)
            putExtra(Constants.SCORE, score)
            putExtra(Constants.TOTAL_QUESTIONS, questionViewModel.questionList.value?.size ?: 0)
            putExtra(Constants.KEY_USER_EMAIL, email)
        }
        startActivity(intent)
        finish()
    }
}
