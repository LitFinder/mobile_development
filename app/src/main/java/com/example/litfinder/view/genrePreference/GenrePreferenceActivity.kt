package com.example.litfinder.view.genrePreference

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.litfinder.databinding.ActivityGenrePreferenceBinding
import com.example.litfinder.remote.api.ApiConfig
import com.example.litfinder.remote.repository.GenreRepository


class GenrePreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenrePreferenceBinding
    private val viewModel: GenrePreferenceViewModel by viewModels()

    private lateinit var genreAdapter: GenreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenrePreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchGenres()

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter(emptyList())
        binding.rvGenres.layoutManager = LinearLayoutManager(this)
        binding.rvGenres.adapter = genreAdapter
    }

    private fun observeViewModel() {
        viewModel.genreNames.observe(this) { genreNames ->
            if (genreNames != null) {
                genreAdapter.updateGenreList(genreNames)
            }
        }
    }
}