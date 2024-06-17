package com.example.litfinder.view.forgotPassword

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityDetailProfileBinding
import com.example.litfinder.databinding.ActivityForgotPasswordBinding
import com.example.litfinder.view.profile.ChangeBioFragment
import com.example.litfinder.view.profile.ChangeNameFragment
import com.example.litfinder.view.profile.ChangePasswordFragment
import com.example.litfinder.view.profile.PersonalDetailsFragment

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(ForgotPasswordFragment())
        }


    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun navigateToForgotPassword() {
        loadFragment(ForgotPasswordFragment())
    }

    fun navigateToVerificationCode() {
        loadFragment(VerificationCodeFragment())
    }

    fun navigateToNewPassword() {
        loadFragment(NewPasswordFragment())
    }

}