package com.example.litfinder.view.discover

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityBasedReviewBinding
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.remote.pref.dataStore
import com.example.litfinder.utils.BookViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BasedReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasedReviewBinding
    private lateinit var bookForYouAdapter: BookForYouAdapter
    private lateinit var bookViewModel: BookViewModel
    private lateinit var userPreference: UserPreferences
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasedReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            setupUserPreference()
            setupRecyclerView()
            setupViewModel()
            observeViewModel()

            binding.shimmerViewContainerForYou.startShimmer()

            userId = userPreference.getUserId().first()
            if (userId != -1) {
                bookViewModel.fetchRecommendationsBasedReview(
                    userId = userId!!,
                    limit = 10,
                    page = 1,
                    search = "true"
                )
            } else {
                Log.e("DiscoverFragment", "User ID is invalid")
            }

            setupScrollListener(binding.contentListBook, bookViewModel)

            binding.backToMain.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

        }
    }

    private fun setupScrollListener(recyclerView: RecyclerView, viewModel: BookViewModel) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Fetch next page
                    val nextPage = viewModel.currentPage + 1
                    userId?.let {
                        viewModel.fetchRecommendationsBasedReview(
                            userId = it,
                            limit = 10,
                            page = nextPage
                        )
                    }
                }
            }
        })
    }

    private fun setupUserPreference() {
        userPreference = UserPreferences.getInstance(dataStore)
    }

    private fun setupRecyclerView() {
        // Initialization of Book New Release Adapter
        bookForYouAdapter = BookForYouAdapter(emptyList())
        binding.contentListBook.adapter = bookForYouAdapter
        binding.contentListBook.layoutManager =
            GridLayoutManager(this, 2) // Change to GridLayoutManager with 2 columns
    }

    private suspend fun setupViewModel() {
        val token = userPreference.getToken().first()
        val factory = BookViewModelFactory { token }
        bookViewModel = ViewModelProvider(this, factory).get(BookViewModel::class.java)
    }

    private fun observeViewModel() {
        bookViewModel.recommendationBasedReview.observe(this, Observer { books ->
            // Hide shimmer
            binding.shimmerViewContainerForYou.stopShimmer()
            binding.shimmerViewContainerForYou.visibility = View.GONE
            binding.contentListBook.visibility = View.VISIBLE

            books?.let {
                bookForYouAdapter.setData(it)
                binding.totalBook.text = getString(R.string.total_list_buku, it.size)
            }
        })
    }
}

