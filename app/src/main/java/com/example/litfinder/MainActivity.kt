package com.example.litfinder

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.litfinder.LoginPutrija.LoginActivity
import com.example.litfinder.LoginPutrija.MainViewModel
import com.example.litfinder.LoginPutrija.User
import com.example.litfinder.LoginPutrija.UserPreferences
import com.example.litfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var userModel: User
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)

        setupAction()
    }

    private fun setupAction() {
        binding.textView3.setOnClickListener {
            userPreferences.logout()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}