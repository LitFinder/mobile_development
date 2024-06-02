package com.example.litfinder.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.litfinder.databinding.FragmentBookshelfBinding

class BookshelfFragment : Fragment() {
    private lateinit var binding: FragmentBookshelfBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookshelfBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}