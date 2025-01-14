package com.example.quizgame.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.quizgame.R
import com.example.quizgame.utils.Constants
import com.example.quizgame.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var btnStart: Button
    private lateinit var edtName: EditText
    private lateinit var txtUseEmail: TextView
    private lateinit var txtUsername: TextView

    private val mainViewModel: MainViewModel by viewModels()

    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.button_start)
        edtName = findViewById(R.id.edtName)
        txtUseEmail = findViewById(R.id.txtUseEmail)
        txtUsername = findViewById(R.id.txtUserName)

        email = intent.getStringExtra(Constants.KEY_USER_EMAIL)

        edtName.visibility = View.GONE
        btnStart.visibility = View.GONE

        mainViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            }
        })

        if (!email.isNullOrEmpty()) {
            txtUseEmail.text = "Login Using $email"
            txtUseEmail.setOnClickListener {
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra(Constants.KEY_USER_EMAIL, email)
                startActivity(intent)
            }
        }

        txtUsername.setOnClickListener {
            edtName.visibility = View.VISIBLE
            btnStart.visibility = View.VISIBLE
        }

        btnStart.setOnClickListener {
            val name = edtName.text.toString().trim()
            if (name.isNotEmpty()) {
                mainViewModel.setLoadingState(true)
                val intent = Intent(this, QuestionActivity::class.java).apply {
                    putExtra(Constants.USER_NAME, name)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG).show()
            }
        }
    }
}
