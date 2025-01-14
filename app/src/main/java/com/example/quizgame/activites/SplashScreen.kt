package com.example.quizgame.activites

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.R
import com.example.quizgame.activities.MainActivity
import com.example.quizgame.activities.TabActivity

class SplashScreen : AppCompatActivity() {
    var handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        handler.postDelayed({


            Log.d("SplashScreen", "Navigating to TabActivity")
            val intent = Intent(this, TabActivity::class.java)
            startActivity(intent)

        } , 3000)
    }
}