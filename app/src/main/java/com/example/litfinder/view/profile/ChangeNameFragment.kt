package com.example.litfinder.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentChangeNameBinding
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class ChangeNameFragment : Fragment() {
    private var _binding: FragmentChangeNameBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel = ViewModelProvider(requireActivity(), factory).get(DetailProfileViewModel::class.java)

        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.editTextName.setText(name)
        }

        binding.back.setOnClickListener {
            (activity as DetailProfileActivity).navigateToPersonalDetails()
        }

        binding.buttonSaveName.setOnClickListener {
            val newName = binding.editTextName.text.toString()
            viewModel.changeUserName(newName)
            viewModel.changeNameResponse.observe(viewLifecycleOwner) { response ->
                if (response.status == "success") {
                    Toast.makeText(requireContext(), "Nama berhasil diubah", Toast.LENGTH_SHORT).show()
                    navigateToDetailProfileActivity()
                } else {
                    Toast.makeText(requireContext(), "Gagal mengubah nama", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.changeNameResponse.observe(viewLifecycleOwner) { response ->
            if (response.status == "success") {
                Toast.makeText(requireContext(), "Nama berhasil diubah", Toast.LENGTH_SHORT).show()
                (activity as DetailProfileActivity).navigateToPersonalDetails()
            } else {
                Toast.makeText(requireContext(), "Gagal mengubah nama", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToDetailProfileActivity() {
        val intent = Intent(requireContext(), DetailProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
