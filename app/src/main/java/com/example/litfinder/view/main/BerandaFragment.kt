package com.example.litfinder.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.litfinder.databinding.FragmentBerandaBinding
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.view.bookPreference.BookAdapter
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class BerandaFragment : Fragment() {
    private lateinit var binding: FragmentBerandaBinding
    private lateinit var bookBerandaAdapter: BookBerandaAdapter
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
        setupRecyclerView(binding.rvBukuUntukmu)
        setupRecyclerView(binding.rvLagiTrending)
        setupRecyclerView(binding.rvBaruRilis)

        viewModel.bookResponse.observe(viewLifecycleOwner) { pagingData ->
            bookBerandaAdapter.submitData(lifecycle, pagingData)
            showLoading(false)
        }

        showLoading(true)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = bookBerandaAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

