package com.example.litfinder.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentBerandaBinding
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.view.bookPreference.BookAdapter
import com.example.litfinder.view.discover.BookForYou
import com.example.litfinder.view.profile.DetailProfileActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class BerandaFragment : Fragment() {
    private lateinit var binding: FragmentBerandaBinding
    private lateinit var bookBerandaAdapter: BookBerandaAdapter
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var recommendationBasedReviewAdapter: RecommendationBasedReviewAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel = ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)

        bookBerandaAdapter = BookBerandaAdapter()
        recommendationAdapter = RecommendationAdapter()
        recommendationBasedReviewAdapter = RecommendationBasedReviewAdapter()

        setupRecyclerView(binding.rvBukuUntukmu, recommendationAdapter)
        setupRecyclerView(binding.rvBasedReview, recommendationBasedReviewAdapter)
        setupRecyclerView(binding.rvBaruRilis, bookBerandaAdapter)

        viewModel.bookResponse.observe(viewLifecycleOwner) { pagingData ->
            bookBerandaAdapter.submitData(lifecycle, pagingData)
            showLoading(false)
        }

        viewModel.recommendationResponse.observe(viewLifecycleOwner) { pagingData ->
            recommendationAdapter.submitData(lifecycle, pagingData)
            showLoading(false)
        }

        viewModel.recommendationBasedReviewResponse.observe(viewLifecycleOwner) { pagingData ->
            recommendationBasedReviewAdapter.submitData(lifecycle, pagingData)
            showLoading(false)
        }

        showLoading(true)

        viewModel.userPhotoUrl.observe(viewLifecycleOwner) { photoUrl ->
            if (photoUrl.isEmpty()) {
                Glide.with(requireContext())
                    .load(R.drawable.default_profile_photo)
                    .into(binding.ivPhoto)
            } else {
                Glide.with(requireContext())
                    .load(photoUrl)
                    .into(binding.ivPhoto)
            }
        }

        recommendationAdapter.setOnBookClickedListener { bookId ->
            viewModel.postLog(bookId)
        }

        bookBerandaAdapter.setOnBookClickedListener { bookId ->
            viewModel.postLog(bookId)
        }

        recommendationBasedReviewAdapter.setOnBookClickedListener { bookId ->
            viewModel.postLog(bookId)
        }


        binding.tvBukuUntukmu.setOnClickListener {
            val intent = Intent(requireContext(), BookForYou::class.java)
            startActivity(intent)
        }

        binding.tvBaruRilis.setOnClickListener {
            val intent = Intent(requireContext(), BookForYou::class.java)
            startActivity(intent)
        }

        binding.tvBasedReview.setOnClickListener {
            val intent = Intent(requireContext(), BookForYou::class.java)
            startActivity(intent)
        }

        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvUsername.text = if (name.isEmpty()) "Nama Pengguna" else name
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUserData() // Reload the user data when the fragment resumes
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
