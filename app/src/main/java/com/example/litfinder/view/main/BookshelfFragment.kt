package com.example.litfinder.view.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentBookshelfBinding
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.utils.BookselfViewModelFactory
import com.example.litfinder.view.bookshelf.BookselfAdapter
import com.example.litfinder.view.bookshelf.BookselfViewModel

class BookshelfFragment : Fragment() {
    private lateinit var binding: FragmentBookshelfBinding
    private lateinit var bookselfAdapter: BookselfAdapter
    private lateinit var bookselfViewModel: BookselfViewModel
    private lateinit var userPreference: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookshelfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserPreference()
        setupViewModel()
        setupRecyclerView()
        setupTabListeners()
        observeViewModel()

        val user = userPreference.getUser()
        if (user.id != -1) {
            showLoading(true)
            bookselfViewModel.fetchBookself(user.id, "all")
        } else {
            Log.e("BookshelfFragment", "User ID is invalid")
        }
    }

    private fun setupUserPreference() {
        context?.let {
            userPreference = UserPreferences(it)
        }
    }

    private fun setupRecyclerView() {
        bookselfAdapter = BookselfAdapter(emptyList())
        binding.contentListBook.adapter = bookselfAdapter
        binding.contentListBook.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupViewModel() {
        val factory = BookselfViewModelFactory { userPreference.getUser().token.toString() }
        bookselfViewModel = ViewModelProvider(this, factory).get(BookselfViewModel::class.java)
    }

    private fun observeViewModel() {
        bookselfViewModel.bookselfItems.observe(viewLifecycleOwner, Observer { items ->
            items?.let {
                bookselfAdapter.setData(it)
                showLoading(false)
                updateTotalBooks(it.size)
            }
        })
    }

    private fun setupTabListeners() {
        binding.frameInginTab.setOnClickListener {
            showLoading(true)
            bookselfViewModel.fetchBookself(userPreference.getUser().id, "want")
            updateTabSelection(binding.frameInginTab)
        }

        binding.frameBacaTab.setOnClickListener {
            showLoading(true)
            bookselfViewModel.fetchBookself(userPreference.getUser().id, "read")
            updateTabSelection(binding.frameBacaTab)
        }

        binding.frameSelesaiTab.setOnClickListener {
            showLoading(true)
            bookselfViewModel.fetchBookself(userPreference.getUser().id, "finish")
            updateTabSelection(binding.frameSelesaiTab)
        }
    }

    private fun updateTabSelection(selectedFrameLayout: FrameLayout) {
        // Reset all backgrounds and text colors
        binding.frameInginTab.setBackgroundResource(R.drawable.shape3)
        binding.contentInginTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary100))

        binding.frameBacaTab.setBackgroundResource(R.drawable.shape3)
        binding.contentBacaTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary100))

        binding.frameSelesaiTab.setBackgroundResource(R.drawable.shape3)
        binding.contentSelesaiTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary100))

        // Set selected background and text color
        selectedFrameLayout.setBackgroundResource(R.drawable.shape)

        // Change text color for the selected tab
        when (selectedFrameLayout) {
            binding.frameInginTab -> {
                binding.contentInginTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            binding.frameBacaTab -> {
                binding.contentBacaTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            binding.frameSelesaiTab -> {
                binding.contentSelesaiTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerViewContainerBookelf.startShimmer()
            binding.shimmerViewContainerBookelf.visibility = View.VISIBLE
            binding.contentListBook.visibility = View.GONE
        } else {
            binding.shimmerViewContainerBookelf.stopShimmer()
            binding.shimmerViewContainerBookelf.visibility = View.GONE
            binding.contentListBook.visibility = View.VISIBLE
        }
    }

    private fun updateTotalBooks(total: Int) {
        binding.totalBook.text = getString(R.string.total_list_buku, total)
    }
}

