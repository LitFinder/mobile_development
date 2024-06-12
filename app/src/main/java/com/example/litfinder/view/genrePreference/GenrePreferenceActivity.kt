package com.example.litfinder.view.genrePreference

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.litfinder.databinding.ActivityGenrePreferenceBinding
import com.example.litfinder.view.bookPreference.BookPreferenceViewModel
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

        // Dummy token for testing, replace with actual token logic
        val token = "your_token_here"
        viewModel.getGenres()
        viewModel.genres.observe(this) { genres ->
            genreAdapter.setData(genres)
        }
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter(emptyList())
        binding.rvGenres.layoutManager = GridLayoutManager(this, 2) // 2 kolom
        binding.rvGenres.adapter = genreAdapter
    }
}
