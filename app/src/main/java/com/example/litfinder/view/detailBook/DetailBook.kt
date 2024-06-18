package com.example.litfinder.view.detailBook

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityDetailBookBinding
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.remote.pref.dataStore
import com.example.litfinder.utils.BookViewModelFactory
import com.example.litfinder.utils.BookselfViewModelFactory
import com.example.litfinder.view.banner.ShareBookshelf
import com.example.litfinder.view.bookshelf.BookselfViewModel
import com.example.litfinder.view.discover.BookAdapter
import com.example.litfinder.view.discover.BookViewModel
import com.taufiqrahman.reviewratings.BarLabels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Random


class DetailBook : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBookBinding
    private lateinit var ratingAdapter: RatingAdapter
    private lateinit var bookAdapter: BookAdapter
    private lateinit var userPreference: UserPreferences
    private lateinit var bookViewModel: BookViewModel
    private lateinit var recomendationBookViewModel: RecomendationBookViewModel
    private lateinit var bookselfViewModel: BookselfViewModel
    private var isDescriptionExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUserPreference()
        setupRecyclerView()
        setupViewModel() // Initialize view models synchronously
        observeViewModel()

        binding.shimmerViewContainerDetail.startShimmer()

        val id = intent.getIntExtra(EXTRA_ID, 0)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val authors = intent.getStringExtra(EXTRA_AUTHORS)
        val image = intent.getStringExtra(EXTRA_IMAGE)
        val publisher = intent.getStringExtra(EXTRA_PUBLISHER)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val previewLink = intent.getStringExtra(EXTRA_PREVIEW_LINK)
        val publishedDate = intent.getStringExtra(EXTRA_PUBLISHED_DATE)
        val infoLink = intent.getStringExtra(EXTRA_INFO_LINK)
        val categories = intent.getStringExtra(EXTRA_CATEGORIES)
        val ratingsCount = intent.getIntExtra(EXTRA_RATINGS_COUNT, 0)
        val status = intent.getStringExtra(EXTRA_STATUS)
        val bookId = intent.getIntExtra(EXTRA_BOOK_ID, -1)

        binding.judulDetail.text = title
        binding.penulisDetail.text = "Penulis : ${authors?.cleanString()}"
        binding.genreDetail.text = "Genre : ${categories?.cleanString()}"
        setupDescriptionText(description)
        binding.imgContent.loadImage(image)

        val statusText = when (status) {
            "want" -> "Ingin"
            "read" -> "Membaca"
            "finish" -> "Selesai"
            else -> "No Action"
        }

        if (bookId != -1) {
            bookselfViewModel.getReviewScores(bookId)
        }

        getRatingBook(bookId)

        binding.status.text = statusText

        binding.status.setOnClickListener {
            when (statusText) {
                "No Action" -> {
                    addBookToBookshelf(bookId)
                    insertLogUser(bookId)
                }
                "Ingin" -> {
                    updateBookToBookshelf(id, "read")
                }
                "Membaca" -> {
                    showReviewDialog(id, "finish")
                }
            }
        }

        binding.backToMain.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }


        binding.shareToSosmed.setOnClickListener {
            val context = it.context
            val intent = ShareBookshelf.newIntent(
                context, title, authors, image, publisher, description, previewLink,
                publishedDate, infoLink, categories, ratingsCount
            )
            context.startActivity(intent)
        }

        val url = infoLink

        binding.buyBook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            intent.setPackage("com.android.chrome")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Jika Google Chrome tidak terinstal, buka di browser default
                intent.setPackage(null)
                startActivity(intent)
            }
        }

        // Fetch books for recommendations
        bookViewModel.fetchBooks(limit = 10, page = 1)
    }

    private fun setupDescriptionText(description: String?) {
        description?.let {
            binding.deskripDetail.text = it
            binding.deskripDetail.post {
                if (binding.deskripDetail.lineCount > 2) {
                    val moreText = "... Selebihnya"
                    val lessText = " Perpendek"
                    val originalText = it
                    val shortText = originalText.substring(
                        0,
                        binding.deskripDetail.layout.getLineEnd(1)
                    ) + moreText

                    val spannableMoreText = SpannableString(shortText).apply {
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            length - moreText.length,
                            length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }

                    val spannableLessText = SpannableString("$originalText$lessText").apply {
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            length - lessText.length,
                            length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }

                    binding.deskripDetail.text = spannableMoreText

                    binding.deskripDetail.setOnClickListener {
                        if (isDescriptionExpanded) {
                            binding.deskripDetail.text = spannableMoreText
                            isDescriptionExpanded = false
                        } else {
                            binding.deskripDetail.text = spannableLessText
                            isDescriptionExpanded = true
                        }
                    }
                }
            }
        }
    }

    private fun getRatingBook(bookId: Int) {
        lifecycleScope.launch {
            userPreference.getUserId().collect { userId ->
                Log.d("idBook", "getRatingBook: $bookId")
                if (userId != -1 && bookId != -1) {
                    bookselfViewModel.getBookToBookshelf(bookId)
                }
            }
        }
    }

    private fun addBookToBookshelf(bookId: Int) {
        lifecycleScope.launch {
            userPreference.getUserId().collect { userId ->
                if (userId != -1 && bookId != -1) {
                    bookselfViewModel.addBookToBookshelf(userId, bookId)
                }
            }
        }
    }

    private fun insertLogUser(bookId: Int) {
        lifecycleScope.launch {
            userPreference.getUserId().collect { userId ->
                if (userId != -1 && bookId != -1) {
                    bookselfViewModel.insertLogUser(userId, bookId)
                }
            }
        }
    }

    private fun updateBookToBookshelf(bookselfId: Int, status: String) {
        lifecycleScope.launch {
            userPreference.getUserId().collect { userId ->
                if (userId != -1) {
                    bookselfViewModel.updateBookToBookshelf(bookselfId, status)
                }
            }
        }
    }

    private fun showReviewDialog(bookselfId: Int, status: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_review, null)
        val reviewHelpfulnessInput = dialogView.findViewById<EditText>(R.id.reviewHelpfulness)
        val reviewScoreInput = dialogView.findViewById<EditText>(R.id.reviewScore)
        val reviewSummaryInput = dialogView.findViewById<EditText>(R.id.reviewSummary)
        val reviewTextInput = dialogView.findViewById<EditText>(R.id.reviewText)

        val builder = AlertDialog.Builder(this, R.style.AlertDialogButton)
        builder.setTitle("Review Book")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val reviewHelpfulness = reviewHelpfulnessInput.text.toString()
                val reviewScore = reviewScoreInput.text.toString().toIntOrNull() ?: 0
                val reviewSummary = reviewSummaryInput.text.toString()
                val reviewText = reviewTextInput.text.toString()
                lifecycleScope.launch {
                    updateBookToBookshelfWithReview(
                        bookselfId,
                        status,
                        reviewHelpfulness,
                        reviewScore,
                        reviewSummary,
                        reviewText
                    )
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateBookToBookshelfWithReview(
        bookselfId: Int,
        status: String,
        reviewHelpfulness: String,
        reviewScore: Int,
        reviewSummary: String,
        reviewText: String
    ) {
        lifecycleScope.launch {
            userPreference.getUser().collect { user ->
                val bookId = intent.getIntExtra(EXTRA_BOOK_ID, -1)
                val profileName = user.name // Replace with actual profile name if available

                if (user.id != -1 && bookId != -1) {
                    bookselfViewModel.updateBookToBookshelfWithReview(
                        bookselfId,
                        status,
                        bookId,
                        user.id,
                        profileName,
                        reviewHelpfulness,
                        reviewScore,
                        reviewSummary,
                        reviewText
                    )
                }
            }
        }
    }

    private fun showAlertNotification(message: String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogButton)
        builder.setTitle("Bookself Update")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun observeViewModel() {
        bookselfViewModel.averageRating.observe(this, Observer { averageRating ->
            binding.averageRating.text = String.format("%.1f", averageRating)
            binding.ratingBar.rating = averageRating
        })

        bookselfViewModel.totalReviews.observe(this, Observer { totalReviews ->
            binding.totalReviews.text = String.format("%,d", totalReviews)
        })

        bookselfViewModel.ratingCounts.observe(this, Observer { ratingCounts ->
            val colors = intArrayOf(
                Color.parseColor("#0e9d58"),
                Color.parseColor("#bfd047"),
                Color.parseColor("#ffc105"),
                Color.parseColor("#ef7e14"),
                Color.parseColor("#d36259")
            )
            binding.ratingReviews.createRatingBars(100, BarLabels.STYPE1, colors, ratingCounts)
        })

        bookselfViewModel.addBookResult.observe(this, Observer { result ->
            val (success, message) = result
            showAlertNotification(message)  // Show alert notification here
        })

        bookselfViewModel.getBookselfItems.observe(this, Observer { items ->
            items?.let { ratingAdapter.setData(it) }
        })

        bookViewModel.books.observe(this, Observer { books ->
            // Hide shimmer
            binding.shimmerViewContainerDetail.stopShimmer()
            binding.shimmerViewContainerDetail.visibility = View.GONE
            binding.recomentFromOthers.visibility = View.VISIBLE

            books?.let { bookAdapter.setData(it) }
        })
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }

    fun String.cleanString(): String {
        return this.replace("[", "")
            .replace("]", "")
            .replace("'", "")
    }

    private fun setupUserPreference() {
        userPreference = UserPreferences.getInstance(dataStore)
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(emptyList())
        binding.recomentFromOthers.adapter = bookAdapter
        binding.recomentFromOthers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        ratingAdapter = RatingAdapter(emptyList())
        binding.recycleviewUlasan.adapter = ratingAdapter
        binding.recycleviewUlasan.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupViewModel() {
        val token = runBlocking { userPreference.getToken().first() }
        val factory = BookViewModelFactory { token }
        bookViewModel = ViewModelProvider(this, factory).get(BookViewModel::class.java)

        bookselfViewModel = ViewModelProvider(
            this@DetailBook,
            BookselfViewModelFactory { token }
        ).get(BookselfViewModel::class.java)
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_AUTHORS = "EXTRA_AUTHORS"
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
        const val EXTRA_PUBLISHER = "EXTRA_PUBLISHER"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_PREVIEW_LINK = "EXTRA_PREVIEW_LINK"
        const val EXTRA_PUBLISHED_DATE = "EXTRA_PUBLISHED_DATE"
        const val EXTRA_INFO_LINK = "EXTRA_INFO_LINK"
        const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"
        const val EXTRA_RATINGS_COUNT = "EXTRA_RATINGS_COUNT"
        const val EXTRA_STATUS = "EXTRA_STATUS"
        const val EXTRA_BOOK_ID = "EXTRA_BOOK_ID"

        fun newIntent(
            context: Context,
            id: Int?,
            title: String?,
            authors: String?,
            image: String?,
            publisher: String?,
            description: String?,
            previewLink: String?,
            publishedDate: String?,
            infoLink: String?,
            categories: String?,
            ratingsCount: Int?,
            status: String?,
            bookId: Int?
        ): Intent {
            return Intent(context, DetailBook::class.java).apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_AUTHORS, authors)
                putExtra(EXTRA_IMAGE, image)
                putExtra(EXTRA_PUBLISHER, publisher)
                putExtra(EXTRA_DESCRIPTION, description)
                putExtra(EXTRA_PREVIEW_LINK, previewLink)
                putExtra(EXTRA_PUBLISHED_DATE, publishedDate)
                putExtra(EXTRA_INFO_LINK, infoLink)
                putExtra(EXTRA_CATEGORIES, categories)
                putExtra(EXTRA_RATINGS_COUNT, ratingsCount)
                putExtra(EXTRA_STATUS, status)
                putExtra(EXTRA_BOOK_ID, bookId)
            }
        }
    }
}


