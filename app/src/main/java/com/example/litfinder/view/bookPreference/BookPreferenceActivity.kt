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

        // Setup the BookAdapter and pass the callback for selected IDs
        bookAdapter = BookAdapter { selectedIds ->
            latestSelectedIds = selectedIds
        }

        setupView()
        setupRecyclerView()
        showLoading(true)
        getData()

        // Handle button click for btnLewati
        binding.btnLewati.setOnClickListener {
            val intent = Intent(this@BookPreferenceActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // Handle button click for btnLanjut
        binding.btnLanjut.setOnClickListener {
            val toastMessage = "Selected IDs: ${latestSelectedIds.joinToString(", ")}"
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        // Set the adapter for the RecyclerView
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
        // Use the same adapter instance for loading data
        binding.rvBook.adapter = bookAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                bookAdapter.retry()
            }
        )
        viewModel.bookResponse.observe(this, {
            bookAdapter.submitData(lifecycle, it)
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
