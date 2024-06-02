package com.example.litfinder.view.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.litfinder.databinding.ActivityRegisterBinding
import com.example.litfinder.remote.api.ApiResponse
import com.example.litfinder.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnRegister?.setOnClickListener {
            val name = binding?.edRegisterUsername?.text.toString()
            val username = binding?.edRegisterUsername?.text.toString()
            val email = binding?.edRegisterEmail?.text.toString()
            val password = binding?.edRegisterPassword?.text.toString()

            val registerRequest = ApiResponse.RegisterRequest(
                name = name,
                username = username,
                email = email,
                password = password
            )

            registerViewModel.registerUser(registerRequest)
        }

        registerViewModel.registerResponse.observe(this, Observer { response ->
            Toast.makeText(this, "Success: ${response.message}", Toast.LENGTH_SHORT).show()
            // Kembali ke LoginActivity setelah berhasil daftar
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })

        registerViewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
        })
    }
}