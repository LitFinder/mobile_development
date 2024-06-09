package com.example.litfinder.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityLoginBinding
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.view.bookPreference.BookPreferenceActivity
import com.example.litfinder.view.genrePreference.GenrePreferenceActivity
import com.example.litfinder.view.main.MainActivity
import com.example.litfinder.view.register.RegisterActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupAction()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill the data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showLoading(true)

            viewModel.login(email, password).observe(this) { response ->
                showLoading(false)
                when (response) {
                    is ApiResponseStatus.Success -> {
                        val token = response.data.token ?: ""
                        if (token.isNotEmpty()) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            viewModel.saveSession(User(email, token))
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(this, "Token not found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    is ApiResponseStatus.Error -> {
                        Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}