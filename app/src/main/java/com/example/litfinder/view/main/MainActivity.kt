package com.example.litfinder.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityMainBinding
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.view.login.LoginActivity

open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferences = UserPreferences(this)

        // Menambah log untuk menampilkan informasi pengguna yang sedang login
        val currentUser = userPreferences.getUser()
        if (currentUser.token!!.isNotEmpty()) {
            Log.d("UserLogin", "User Token: ${currentUser.token}")
        } else {
            Log.d("UserLogin", "No user logged in")
        }

        userPreferences = UserPreferences(this)

        if (!isLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

//        setupAction()
    }

    private fun isLoggedIn(): Boolean {
        val user = userPreferences.getUser()
        return !user.token.isNullOrEmpty()
    }

//    private fun setupAction() {
//        binding.navContentAvtivity.setOnClickListener {
//            userPreferences.logout()
//
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_content_avtivity, fragment)
        transaction.commit()
    }
}