package com.example.litfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.litfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        loadFragment(HomeFragment())

//        binding.bottomNav.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.home -> {
//                    loadFragment(HomeFragment())
//                    true
//                }
//                R.id.search -> {
//                    loadFragment(ChatFragment())
//                    true
//                }
//                R.id.bookshelf -> {
//                    loadFragment(SettingFragment())
//                    true
//                }
//                R.id.account -> {
//                    loadFragment(SettingFragment())
//                    true
//                }
//                else -> false
//            }
//        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}