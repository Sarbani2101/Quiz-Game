package com.example.quizgame.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.R
import com.example.quizgame.utils.Constants

class ResultActivity : AppCompatActivity() {

    private lateinit var textViewScore: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var finishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        textViewScore = findViewById(R.id.textview_score)
        textViewName = findViewById(R.id.textview_name)
        textViewEmail = findViewById(R.id.textview_email)
        finishButton = findViewById(R.id.finish_button)

        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        val score = intent.getIntExtra(Constants.SCORE, 0)
        val email = intent.getStringExtra(Constants.KEY_USER_EMAIL) ?: ""
        val name = intent.getStringExtra(Constants.USER_NAME) ?: ""

        textViewScore.text = getString(R.string.score_message, score, totalQuestions)
        textViewEmail.text = email
        textViewName.text = name

        if (email.isNotEmpty()){
            textViewEmail.visibility = View.VISIBLE
        }
        else {
            textViewName.visibility = View.VISIBLE
        }

        finishButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
