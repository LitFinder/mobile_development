package com.example.litfinder.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentPersonalDetailsBinding
import com.example.litfinder.view.main.MainActivity
import com.example.litfinder.view.main.MainViewModel
import com.example.litfinder.view.main.ProfileFragment
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class PersonalDetailsFragment : Fragment() {
    private var _binding: FragmentPersonalDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel = ViewModelProvider(requireActivity(), factory).get(DetailProfileViewModel::class.java)

        binding.changeName.setOnClickListener {
            (activity as DetailProfileActivity).navigateToChangeName()
        }

        binding.changeBio.setOnClickListener {
            (activity as DetailProfileActivity).navigateToChangeBio()
        }

        binding.changePassword.setOnClickListener {
            (activity as DetailProfileActivity).navigateToChangePassword()
        }

        binding.backToProfile.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvName.text = name
        }

        viewModel.userBio.observe(viewLifecycleOwner) { bio ->
            if (bio.isNullOrEmpty()) {
                binding.tvBio.text = "Bio masih kosong"
                binding.tvBio.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey)) // Sesuaikan dengan warna abu-abu yang Anda miliki
            } else {
                binding.tvBio.text = bio
                binding.tvBio.setTextColor(ContextCompat.getColor(requireContext(), R.color.black)) // Atur warna teks kembali ke warna hitam
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
