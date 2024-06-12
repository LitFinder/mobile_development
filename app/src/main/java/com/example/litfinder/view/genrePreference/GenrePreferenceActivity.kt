package com.example.litfinder.view.genrePreference

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        viewModel.getGenres()
        viewModel.genres.observe(this) { genres ->
            genreAdapter.setData(genres)
        }

        binding.btnLanjut.setOnClickListener {
            val userId = 1 // TODO: Replace with actual user id
            val selectedGenreIds = genreAdapter.getSelectedGenreIds()
            Log.d("GENREIDNYA", "$selectedGenreIds")
            viewModel.addGenrePreference( userId, selectedGenreIds.toList())
        }

        binding.btnLewati.setOnClickListener {
            val intent = Intent(this@GenrePreferenceActivity, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

//        viewModel.postResponse.observe(this) { response ->
//            Toast.makeText(this, "Post Response: ${response.status}", Toast.LENGTH_SHORT).show()
//            // TODO: Handle response as needed
//        }
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter(emptyList())
        binding.rvGenres.layoutManager = GridLayoutManager(this, 2) // 2 kolom
        binding.rvGenres.adapter = genreAdapter
    }
}

