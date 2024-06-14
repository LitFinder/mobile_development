package com.example.litfinder.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentPersonalDetailsBinding

class PersonalDetailsFragment : Fragment() {
    private var _binding: FragmentPersonalDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeName.setOnClickListener {
            (activity as DetailProfileActivity).navigateToChangeName()
        }

        binding.changeBio.setOnClickListener {
            (activity as DetailProfileActivity).navigateToChangeBio()
        }

        binding.changePassword.setOnClickListener {
            (activity as DetailProfileActivity).navigateToChangePassword()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
