package com.example.litfinder.view.bookPreference

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
import com.example.litfinder.databinding.ActivityBookPreferenceBinding
import com.example.litfinder.remote.adapter.LoadingStateAdapter
import com.example.litfinder.view.login.LoginViewModel
import com.example.litfinder.view.main.MainActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class BookPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookPreferenceBinding
    private val viewModel by viewModels<BookPreferenceViewModel> { ViewModelFactory(this) }
    private lateinit var bookAdapter: BookAdapter
    private var latestSelectedIds: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookAdapter = BookAdapter { selectedIds ->
            latestSelectedIds = selectedIds
        }

        setupView()
        setupRecyclerView()
        showLoading(true)
        getData()

        binding.btnLewati.setOnClickListener {
            val intent = Intent(this@BookPreferenceActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnLanjut.setOnClickListener {
            postBookPreferences()
        }

//        viewModel.postBookResponse.observe(this) { response ->
//            if (response != null) {
//                Toast.makeText(this, response.data ?: "Preferences saved successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Failed to save preferences", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        viewModel.errorMessage.observe(this) { errorMessage ->
//            if (errorMessage != null) {
//                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
//            }
//        }
    }

    private fun setupRecyclerView() {
        binding.rvBook.adapter = bookAdapter
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

    private fun getData() {
        showLoading(false)
        binding.rvBook.adapter = bookAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                bookAdapter.retry()
            }
        )
        viewModel.bookResponse.observe(this) {
            bookAdapter.submitData(lifecycle, it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun postBookPreferences() {
        val userId = 1 // Ganti dengan user_id yang sebenarnya
        val books = latestSelectedIds.map { it.toInt() }

//        Log.d("AddBookPreference", "Token: $token")
        Log.d("AddBookPreference", "User ID: $userId")
        Log.d("AddBookPreference", "Books: $books")

//        val userId = 1
//        val books = listOf(5, 33)
        viewModel.addBookPreference(userId, books)
    }
}