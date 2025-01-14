package com.example.quizgame.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quizgame.R
import com.example.quizgame.activities.TabActivity
import com.example.quizgame.databinding.FragmentRegisterBinding
import com.example.quizgame.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        registerViewModel.registrationStatus.observe(viewLifecycleOwner) { isSuccess ->
            binding.progressBar.visibility = View.GONE
            if (isSuccess) {
                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                (activity as? TabActivity)?.switchToLoginTab()
            } else {
                showError("Registration Failed. Please try again.")
            }
        }

        registerViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            binding.progressBar.visibility = View.GONE
            showError(errorMessage)
        }

        registerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.btnUp.setOnClickListener {
            binding.errorText.visibility = View.GONE
            val fullName = binding.edtName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val phone = binding.edtPhn.text.toString().trim()
            val password = binding.edtPass.text.toString().trim()
            val confirmPassword = binding.edtRepass.text.toString().trim()

            if (validateInputs(fullName, email, phone, password, confirmPassword)) {
                registerViewModel.registerUser(fullName, email, phone, password, confirmPassword)
            }
        }

        binding.txtLogin.setOnClickListener {
            (activity as? TabActivity)?.switchToLoginTab()
        }
    }

    private fun validateInputs(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return when {
            fullName.isEmpty() -> {
                showError("Full name is required")
                false
            }
            email.isEmpty() -> {
                showError("Email is required")
                false
            }
            phone.isEmpty() -> {
                showError("Phone number is required")
                false
            }
            password.isEmpty() -> {
                showError("Password is required")
                false
            }
            confirmPassword.isEmpty() -> {
                showError("Please confirm your password")
                false
            }
            password != confirmPassword -> {
                showError("Passwords do not match")
                false
            }
            else -> true
        }
    }

    private fun showError(message: String) {
        binding.errorText.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnRegisterSuccessListener {
        fun onRegisterSuccess()
    }
}
