package com.example.litfinder.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.litfinder.view.main.BerandaFragment
import com.example.litfinder.view.main.BookshelfFragment
import com.example.litfinder.view.main.DiscoverFragment
import com.example.litfinder.view.main.ProfileFragment
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityMainBinding
import com.example.litfinder.view.login.LoginActivity

open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_content_avtivity, fragment)
        transaction.commit()
    }
}