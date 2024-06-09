package com.example.litfinder.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityMainBinding
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.view.bookPreference.BookPreferenceActivity
import com.example.litfinder.view.login.LoginActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, BookPreferenceActivity::class.java))
            }
        }

        setupView()
        observeLogout()

        loadFragment(BerandaFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(BerandaFragment())
                    true
                }
                R.id.search -> {
                    loadFragment(DiscoverFragment())
                    true
                }
                R.id.bookshelf -> {
                    loadFragment(BookshelfFragment())
                    true
                }
                R.id.account -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
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
    }

    private fun observeLogout() {
        viewModel.logoutResult.observe(this) { isLoggedOut ->
            if (isLoggedOut) {
                Toast.makeText(this, getString(R.string.logout_success_message), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

//    private fun isLoggedIn(): Boolean {
//        val user = userPreferences.getUser()
//        return !user.token.isNullOrEmpty()
//    }

    private fun setupAction() {
        binding.navContentAvtivity.setOnClickListener {
            viewModel.logout()

            val intent = Intent(this, BookPreferenceActivity::class.java)
            startActivity(intent)
//            finish()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_content_avtivity, fragment)
        transaction.commit()
    }
}