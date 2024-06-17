package com.example.litfinder.view.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.litfinder.databinding.FragmentDiscoverBinding
import com.example.litfinder.remote.api.DataItemtype
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.utils.BookViewModelFactory
import com.example.litfinder.utils.TypeGenreViewModelFactory
import com.example.litfinder.view.discover.BookAdapter
import com.example.litfinder.view.discover.BookRatingsUpAdapter
import com.example.litfinder.view.discover.BookViewModel
import com.example.litfinder.view.discover.TypeGenreAdapter
import com.example.litfinder.view.discover.TypeGenreViewModel

class DiscoverFragment : Fragment() {
    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookRatingsUpAdapter: BookRatingsUpAdapter
    private lateinit var bookViewModel: BookViewModel
    private lateinit var userPreference: UserPreferences
    private lateinit var genreViewModel: TypeGenreViewModel
    private lateinit var genreAdapter: TypeGenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserPreference()
        setupRecyclerView()
        setupViewModel()
        observeViewModel()
        setupSearch()

        // Show shimmer
        binding.shimmerViewContainer.startShimmer()
        binding.shimmerViewContainerBookRating.startShimmer()

        // Fetch the books with parameters
        bookViewModel.fetchBooks(limit = 10, page = 1, search = "true")
        binding.contentListBook.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Fetch next page
                    val nextPage = bookViewModel.currentPage + 1
                    bookViewModel.fetchBooks(limit = 10, page = nextPage)
                }
            }
        })

        binding.contentListRatingTertinggi.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Fetch next page
                    val nextPage = bookViewModel.currentPage + 1
                    bookViewModel.fetchBooks(limit = 10, page = nextPage)
                }
            }
        })
    }

    private fun setupUserPreference() {
        context?.let {
            userPreference = UserPreferences(it)
        }
    }

    private fun setupRecyclerView() {
        // Inisialitation Book New Release
        bookAdapter = BookAdapter(emptyList())
        binding.contentListBook.adapter = bookAdapter
        binding.contentListBook.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Inisialitation Book Rating Up
        bookRatingsUpAdapter = BookRatingsUpAdapter(emptyList())
        binding.contentListRatingTertinggi.adapter = bookRatingsUpAdapter
        binding.contentListRatingTertinggi.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Inisialitation type Genre
        genreAdapter = TypeGenreAdapter(emptyList()) { genre ->
            onGenreSelected(genre)
        }
        binding.contentTabSearch.adapter = genreAdapter
        binding.contentTabSearch.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupViewModel() {
        val factory = BookViewModelFactory { userPreference.getUser().token.toString() }
        bookViewModel = ViewModelProvider(this, factory).get(BookViewModel::class.java)

        val genreFactory = TypeGenreViewModelFactory { userPreference.getUser().token.toString() }
        genreViewModel = ViewModelProvider(this, genreFactory).get(TypeGenreViewModel::class.java)
    }

    private fun observeViewModel() {
        bookViewModel.books.observe(viewLifecycleOwner, Observer { books ->
            // Hide shimmer
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            binding.contentListBook.visibility = View.VISIBLE

            books?.let { bookAdapter.setData(it) }
        })

        bookViewModel.books.observe(viewLifecycleOwner, Observer { books ->
            // Hide shimmer
            binding.shimmerViewContainerBookRating.stopShimmer()
            binding.shimmerViewContainerBookRating.visibility = View.GONE
            binding.contentListRatingTertinggi.visibility = View.VISIBLE

            books?.let { bookRatingsUpAdapter.setData(it) }
        })

        genreViewModel.genres.observe(viewLifecycleOwner, Observer { genres ->
            // Update genre list
            genres?.let { genreAdapter.setData(it) }
        })
    }

    private fun setupSearch() {
        binding.fieldKodeProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Fetch books based on search query
                val query = s.toString()
                bookViewModel.fetchBooks(limit = 10, page = 1, search = query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun onGenreSelected(genre: DataItemtype) {
        // Filter books based on selected genre
        val genreName = genre.name
        bookViewModel.fetchBooks(limit = 10, page = 1, search = genreName)
    }
}








