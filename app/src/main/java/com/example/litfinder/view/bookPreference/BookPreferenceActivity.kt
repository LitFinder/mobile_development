package com.example.litfinder.view.bookPreference

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.litfinder.databinding.ActivityBookPreferenceBinding
import com.example.litfinder.databinding.ActivityGenrePreferenceBinding
import com.example.litfinder.view.genrePreference.GenreAdapter
import com.example.litfinder.view.genrePreference.GenrePreferenceViewModel
import com.example.litfinder.view.main.MainActivity

class BookPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookPreferenceBinding
    private val viewModel: BookPreferenceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val userPreferences = UserPreferences(context)
        viewModel.getBooks()
//
//        // Observasi data buku dari ViewModel
//        viewModel.books.observe(this, { books ->
//            // Lakukan sesuatu dengan data buku, misalnya tampilkan di UI
//            books.forEach { book ->
//                val bookId = book.id
//                val bookImage = book.image
//                // Gunakan data id buku dan data gambar buku sesuai kebutuhan
//            }
//        })

        binding.btnLewati.setOnClickListener {
            val intent = Intent(this@BookPreferenceActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}