package com.example.litfinder.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentChangePasswordBinding
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(DetailProfileViewModel::class.java)

//        viewModel.userName.observe(viewLifecycleOwner) { name ->
//            binding.editTextName.setText(name)
//        }

        binding.back.setOnClickListener {
            (activity as DetailProfileActivity).navigateToPersonalDetails()
        }


        binding.buttonSavePassword.setOnClickListener {
            // Handle saving new name
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
