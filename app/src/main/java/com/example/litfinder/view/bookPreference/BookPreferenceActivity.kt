package com.example.litfinder.view.bookPreference

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
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
//        getData()

        observeData()

        setupSearchBar()
        getData()

        binding.btnLewati.setOnClickListener {
            val intent = Intent(this@BookPreferenceActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnLanjut.setOnClickListener {
            postBookPreferences()
        }

        bookAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Loading) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        }

        viewModel.bookResponse.observe(this) { pagingData ->
            bookAdapter.submitData(lifecycle, pagingData)
            binding.progressBar.visibility = View.GONE
        }

        viewModel.bookResponse.observe(this) {
            if (it == null) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }


    }

    private fun setupRecyclerView() {
        binding.rvBook.adapter = bookAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                bookAdapter.retry()
            }
        )
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

//    private fun getData() {
//        binding.rvBook.adapter = bookAdapter.withLoadStateFooter(
//            footer = LoadingStateAdapter {
//                bookAdapter.retry()
//            }
//        )
//        viewModel.bookResponse.observe(this) {
//            bookAdapter.submitData(lifecycle, it)
//        }
//    }

    private fun getData() {
        binding.rvBook.adapter = bookAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                bookAdapter.retry()
            }
        )
        viewModel.bookResponse.observe(this) { pagingData ->
            bookAdapter.submitData(lifecycle, pagingData)
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

    private fun observeData() {
        viewModel.bookResponse.observe(this) {
            bookAdapter.submitData(lifecycle, it)
        }
    }

    private fun setupSearchBar() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.trim().toString()
                if (query.isNotEmpty()) {
                    viewModel.updateSearchQuery(query)
                }
                searchView.setupWithSearchBar(searchBar)
                searchView.hide()
                true
            }
        }
    }

}