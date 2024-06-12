package com.example.litfinder.view.genrePreference

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.litfinder.databinding.ActivityGenrePreferenceBinding
import com.example.litfinder.view.bookPreference.BookPreferenceViewModel
import com.example.litfinder.view.main.MainActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory


class GenrePreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenrePreferenceBinding
    private val viewModel by viewModels<GenrePreferenceViewModel> { ViewModelFactory(this) }
    private lateinit var genreAdapter: GenreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenrePreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        viewModel.getGenres()
        viewModel.genres.observe(this) { genres ->
            genreAdapter.setData(genres)
            showLoading(false) // Hide loading when data is loaded
        }

        binding.btnLanjut.setOnClickListener {
            val selectedGenreIds = genreAdapter.getSelectedGenreIds()
            Log.d("GENREIDNYA", "$selectedGenreIds")

            if (selectedGenreIds.isEmpty()) {
                Toast.makeText(this, "Silahkan pilih Genre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showLoading(true) // Show loading when adding genre preferences
            viewModel.addGenrePreference(selectedGenreIds.toList())
        }

        binding.btnLewati.setOnClickListener {
            val intent = Intent(this@GenrePreferenceActivity, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter(emptyList())
        binding.rvGenres.layoutManager = GridLayoutManager(this, 2) // 2 kolom
        binding.rvGenres.adapter = genreAdapter
    }

    private fun observeViewModel() {
        viewModel.postResponse.observe(this) { response ->
            showLoading(false) // Hide loading after adding genre preferences
            if (response.status == "success") {
                navigateToMainActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
