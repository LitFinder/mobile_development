package com.example.litfinder.view.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentBerandaBinding
import com.example.litfinder.view.discover.BasedReviewActivity
import com.example.litfinder.view.discover.BookForYou
import com.example.litfinder.view.discover.BookViewModel
import com.example.litfinder.view.discover.NewReleasedActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class BerandaFragment : Fragment() {
    private lateinit var binding: FragmentBerandaBinding
    private lateinit var bookBerandaAdapter: BookBerandaAdapter
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var recommendationBasedReviewAdapter: RecommendationBasedReviewAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var bookViewModel: BookViewModel

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
//        setupRecyclerView(binding.rvBaruRilis, bookBerandaAdapter)

//        viewModel.bookResponse.observe(viewLifecycleOwner) { pagingData ->
//            bookBerandaAdapter.submitData(lifecycle, pagingData)
//        }

        viewModel.recommendationResponse.observe(viewLifecycleOwner) { pagingData ->
            recommendationAdapter.submitData(lifecycle, pagingData)
        }

        viewModel.recommendationBasedReviewResponse.observe(viewLifecycleOwner) { pagingData ->
            recommendationBasedReviewAdapter.submitData(lifecycle, pagingData)
        }

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

//        bookBerandaAdapter.setOnBookClickedListener { bookId ->
//            viewModel.postLog(bookId)
//        }

        recommendationBasedReviewAdapter.setOnBookClickedListener { bookId ->
            viewModel.postLog(bookId)
        }

        binding.tvBukuUntukmu.setOnClickListener {
            val intent = Intent(requireContext(), BookForYou::class.java)
            startActivity(intent)
        }

        binding.tvBaruRilis.setOnClickListener {
            val intent = Intent(requireContext(), NewReleasedActivity::class.java)
            startActivity(intent)
        }

        binding.tvBasedReview.setOnClickListener {
            val intent = Intent(requireContext(), BasedReviewActivity::class.java)
            startActivity(intent)
        }

        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvUsername.text = if (name.isEmpty()) "Nama Pengguna" else name
        }

        // Memuat data dengan efek shimmer
        loadData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUserData() // Reload the user data when the fragment resumes
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    private fun loadData() {
        // Simulasi delay untuk menampilkan efek shimmer
        Handler(Looper.getMainLooper()).postDelayed({
            // Berhenti efek shimmer dan tampilkan RecyclerView
            binding.shimmerViewContainerBukuUntukmu.stopShimmer()
            binding.shimmerViewContainerBukuUntukmu.visibility = View.GONE
            binding.rvBukuUntukmu.visibility = View.VISIBLE

            binding.shimmerViewContainerBasedReview.stopShimmer()
            binding.shimmerViewContainerBasedReview.visibility = View.GONE
            binding.rvBasedReview.visibility = View.VISIBLE

            binding.shimmerViewContainerBaruRilis.stopShimmer()
            binding.shimmerViewContainerBaruRilis.visibility = View.GONE
            binding.rvBaruRilis.visibility = View.VISIBLE
        }, 2000) // Durasi delay dalam milidetik
    }
}
