package com.example.litfinder.view.genrePreference

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.litfinder.databinding.ActivityGenrePreferenceBinding
import com.example.litfinder.remote.response.GenreItem
import com.example.litfinder.view.main.MainActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory
import kotlinx.coroutines.launch


class GenrePreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenrePreferenceBinding
    private val viewModel by viewModels<GenrePreferenceViewModel> { ViewModelFactory(this) }
    private lateinit var genreAdapter: GenreAdapter
    private val genres = getManualGenres()
    private val selectedGenreIds = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenrePreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            val idselected = viewModel.loadUserGenreIds()

            setupRecyclerView()
            observeViewModel()

            genreAdapter.setData(genres)

            autoSelectGenres(idselected)
        }

        binding.btnLanjut.setOnClickListener {
            val selectedGenreIds = genreAdapter.getSelectedGenreIds()
            Log.d("GENREIDNYA", "$selectedGenreIds")

            if (selectedGenreIds.isEmpty()) {
                Toast.makeText(this, "Silahkan pilih Genre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showLoading(true)
            viewModel.addGenrePreference(selectedGenreIds.toList())
        }

        binding.btnLewati.setOnClickListener {
            val intent = Intent(this@GenrePreferenceActivity, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter(genres)
        binding.rvGenres.layoutManager = GridLayoutManager(this, 2)
        binding.rvGenres.adapter = genreAdapter
    }

    private fun observeViewModel() {
        viewModel.postResponse.observe(this) { response ->
            showLoading(false)
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

    private fun getManualGenres(): List<GenreItem> {
        return listOf(
            GenreItem(11, "Fiction"),
            GenreItem(4, "Juvenile Fiction"),
            GenreItem(1, "Religion"),
            GenreItem(3, "History"),
            GenreItem(2, "Biography & Autobiography"),
            GenreItem(15, "Business & Economics"),
            GenreItem(16, "Juvenile Nonfiction"),
            GenreItem(31, "Computers"),
            GenreItem(35, "Social Science"),
            GenreItem(23, "Science"),
            GenreItem(24, "Cooking"),
            GenreItem(14, "Body, Mind & Spirit"),
            GenreItem(10, "Health & Fitness"),
            GenreItem(8, "Family & Relationships"),
            GenreItem(38, "Philosophy")
        )
    }

    private fun autoSelectGenres(genreIds: List<Int>) {
        genreIds.forEach { id ->
            val index = genres.indexOfFirst { it.id == id }
            if (index != -1) {
                selectedGenreIds.add(id)
                genreAdapter.setSelectedGenreId(id)
                genreAdapter.notifyItemChanged(index)
            }
        }
    }

}
