package com.example.litfinder.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityLoginBinding
import com.example.litfinder.databinding.ActivityRegisterBinding
import com.example.litfinder.remote.api.ApiResponse
import com.example.litfinder.utils.isValidEmail
import com.example.litfinder.view.bookPreference.BookPreferenceActivity
import com.example.litfinder.view.genrePreference.GenrePreferenceActivity
import com.example.litfinder.view.login.LoginActivity
import com.example.litfinder.view.login.LoginViewModel
import com.example.litfinder.view.viewModelFactory.ViewModelFactory
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
//        setupAction()
        binding.btnLogin.setOnClickListener { navigateToLoginActivity() }

        viewModel.navigateToBookPreference.observe(this) { navigate ->
            if (navigate) {
                navigateToBookPreferenceActivity()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        }

        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
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

//    private fun setupAction() {
//        binding.btnRegister.setOnClickListener {
//            val email = binding.edRegisterEmail.text.toString()
//            val name = binding.edRegisterName.text.toString()
//            val username = binding.edRegisterUsername.text.toString()
//            val password = binding.edRegisterPassword.text.toString()
//
//            if (email.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Please fill the data", Toast.LENGTH_SHORT).show()
//            } else if (!isValidEmail(email)) {
//                binding.edRegisterEmail.setError(getString(R.string.email_invalid_message))
//            } else if (password.length < 8) {
//                binding.edRegisterPassword.setError(getString(R.string.password_length_message))
//            } else {
//                showLoading(true)
//                viewModel.registerUser(name, username, email, password)
//            }
//        }
//    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToBookPreferenceActivity() {
        val intent = Intent(this, BookPreferenceActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}