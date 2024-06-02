package com.example.litfinder.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityLoginBinding
import com.example.litfinder.view.main.MainActivity
import com.example.litfinder.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var userModel: User = User()
    private lateinit var userPreference: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreferences(this)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val user = userPreference.getUser()
        if (user.token!!.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            viewModel.isLoading.observe(this) {
                showLoading(it)
            }

            login()

            viewModel.errorMessage.observe(this) { errorMessage ->
                if (errorMessage.isNotEmpty()) {
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

//        viewModel.isLoading.observe(this) {
//            showLoading(it)
//        }
//
////        signUp()
//        login()
//
//        viewModel.errorMessage.observe(this) { errorMessage ->
//            if (errorMessage.isNotEmpty()) {
//                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }



//    private fun signUp() {
//        binding.btnRegister.setOnClickListener {
//            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//    }

    private fun login() {
        val edLoginEmail = binding.edLoginEmail.text
        val edLoginPassword = binding.edLoginPassword.text

        binding.btnLogin.setOnClickListener {
            if (edLoginEmail!!.isEmpty() || edLoginPassword!!.isEmpty()) {
                showToast(R.string.empty_form)
//            } else if (!isValidEmail(edLoginEmail.toString()) || edLoginPassword.length < 8) {
//                showToast(R.string.invalid_form)
            } else {
                viewModel.login(
                    edLoginEmail.toString(),
                    edLoginPassword.toString()
                )

                viewModel.isSuccess.observe(this) {
                    if (it) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

//                viewModel.isSuccess.observe(this) {
//                    if (it) {
//                        Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
//                    }
//                }

                viewModel.loginResult.observe(this) { result ->
                    result.data?.let { data ->
                        userModel.token = result.token
                        userPreference.setUser(userModel)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
    }

    private fun showToast(message: Int) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }
}