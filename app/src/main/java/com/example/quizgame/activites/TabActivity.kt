package com.example.quizgame.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.quizgame.R
import com.example.quizgame.adapter.ViewpagerAdapter
import com.example.quizgame.fragment.LoginFragment
import com.example.quizgame.fragment.RegisterFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabActivity : AppCompatActivity(),
    LoginFragment.OnLoginSuccessListener,
    RegisterFragment.OnRegisterSuccessListener {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        viewPager.adapter = ViewpagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Login"
                1 -> tab.text = "Register"
            }
        }.attach()
    }

    fun switchToLoginTab() {
        viewPager.currentItem = 0
    }

    fun switchToRegisterTab() {
        viewPager.currentItem = 1
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginSuccess(email: String) {
        navigateToMainActivity()
    }

    override fun onRegisterSuccess() {
        navigateToMainActivity()
    }
}
