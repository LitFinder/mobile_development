package com.example.litfinder.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentChangeBioBinding
import com.example.litfinder.databinding.FragmentChangePasswordBinding

class ChangeBioFragment : Fragment() {
    private var _binding: FragmentChangeBioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeBioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
