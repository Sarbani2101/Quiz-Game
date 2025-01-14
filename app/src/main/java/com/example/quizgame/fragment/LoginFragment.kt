package com.example.quizgame.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quizgame.activities.MainActivity
import com.example.quizgame.activities.TabActivity
import com.example.quizgame.databinding.FragmentLoginBinding
import com.example.quizgame.utils.Constants
import com.example.quizgame.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        loginViewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                loginViewModel.userDetails.observe(viewLifecycleOwner) { user ->
                    user?.let {
                        navigateToMainActivity(it.email)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        loginViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            binding.errorText.text = message
            binding.errorText.visibility = if (!message.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.btnIn.setOnClickListener {
            val email = binding.edtMail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (validateInput(email, password)) {
                binding.errorText.visibility = View.GONE
                loginViewModel.loginUser(email, password)
            }
        }

        binding.txtReg.apply {
            isClickable = true
            setOnClickListener {
                (activity as? TabActivity)?.switchToRegisterTab()
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Email and Password are required", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun navigateToMainActivity(email: String) {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            putExtra(Constants.KEY_USER_EMAIL, email)
        }
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnLoginSuccessListener {
        fun onLoginSuccess(email: String)
    }
}
