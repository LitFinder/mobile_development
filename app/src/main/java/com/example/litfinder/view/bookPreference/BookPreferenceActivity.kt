package com.example.litfinder.view.bookPreference

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.litfinder.databinding.ActivityBookPreferenceBinding
import com.example.litfinder.databinding.ActivityGenrePreferenceBinding
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.view.genrePreference.GenreAdapter
import com.example.litfinder.view.genrePreference.GenrePreferenceViewModel
import com.example.litfinder.view.login.LoginViewModel
import com.example.litfinder.view.main.MainActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookPreferenceBinding
    private val viewModel by viewModels<BookPreferenceViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.fetchBooks( 10, 1)

        viewModel.bookResponse.observe(this) { response ->
            when (response) {
                is ApiResponseStatus.Success -> {
                    val bookResponse = response.data
                    Log.d("BookActivity", "Book Response: $bookResponse")
                }
                is ApiResponseStatus.Error -> {
                    val errorMessage = response.errorMessage
                    Log.e("BookActivity", "Error: $errorMessage")
                }
                ApiResponseStatus.Loading -> {
                    Log.d("BookActivity", "Loading...")
                }
            }
        }

        binding.btnLewati.setOnClickListener {
            val intent = Intent(this@BookPreferenceActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }


}