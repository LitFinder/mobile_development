package com.example.litfinder.view.bookPreference

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.GridView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.example.litfinder.databinding.ActivityBookPreferenceBinding
import com.example.litfinder.databinding.ActivityGenrePreferenceBinding
import com.example.litfinder.remote.adapter.LoadingStateAdapter
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.response.BookItem
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

    private lateinit var gridViewBook: GridView
    private var bookList: List<BookItem> = emptyList() // Inisialisasi properti bookList dengan emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        showLoading(true)
        getData()



        binding.btnLewati.setOnClickListener {
            val intent = Intent(this@BookPreferenceActivity, MainActivity::class.java)
            startActivity(intent)
        }

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
        val adapter = BookAdapter()
        binding.rvBook.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.bookResponse.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

//        viewModel.fetchBooks(10, 1)

//        viewModel.bookResponse.observe(this) { response ->
//            when (response) {
//                is ApiResponseStatus.Success -> {
//                    val bookResponse = response.data
//                    val bookItems = bookResponse?.data // Ambil daftar data buku dari respons API
//
//                    // Konversi data buku ke objek BookItem
//                    val convertedBookList = bookItems?.map { bookData ->
//                        BookItem(
//                            image = bookData?.image,
//                            previewLink = bookData?.previewLink,
//                            rating = bookData?.rating,
//                            description = bookData?.description,
//                            publisher = bookData?.publisher,
//                            ratingsCount = bookData?.ratingsCount,
//                            id = bookData?.id,
//                            publishedDate = bookData?.publishedDate,
//                            categories = bookData?.categories,
//                            title = bookData?.title,
//                            authors = bookData?.authors,
//                            infoLink = bookData?.infoLink
//                        )
//                    }
//
//                    // Inisialisasi bookList dengan data buku yang telah dikonversi
//                    bookList = convertedBookList.orEmpty() // Menggunakan orEmpty() untuk mengatasi nullable
//
//                    showLoading(false)
//
//                    // Setelah bookList diisi, Anda dapat memperbarui tampilan GridView
//                    val bookAdapter = BookAdapter(this, bookList)
//                    gridViewBook.adapter = bookAdapter
//                }
//                is ApiResponseStatus.Error -> {
//                    val errorMessage = response.errorMessage
//                    Log.e("BookActivity", "Error: $errorMessage")
//                    showLoading(false)
//                }
//                ApiResponseStatus.Loading -> {
//                    Log.d("BookActivity", "Loading...")
//                    showLoading(true)
//                }
//            }
//        }