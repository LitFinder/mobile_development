package com.example.litfinder.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.databinding.FragmentChangeBioBinding
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class ChangeBioFragment : Fragment() {
    private var _binding: FragmentChangeBioBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeBioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(DetailProfileViewModel::class.java)

        viewModel.userBio.observe(viewLifecycleOwner) { bio ->
            binding.editTextBio.setText(bio)
        }

        binding.back.setOnClickListener {
            (activity as DetailProfileActivity).navigateToPersonalDetails()
        }

        binding.buttonSaveBio.setOnClickListener {
            val newBio = binding.editTextBio.text.toString()
            viewModel.changeUserBio(newBio)
            viewModel.changeBioResponse.observe(viewLifecycleOwner) { response ->
                if (response.status == "success") {
                    Toast.makeText(requireContext(), "Bio berhasil diubah", Toast.LENGTH_SHORT)
                        .show()
                    navigateToDetailProfileActivity()
                } else {
                    Toast.makeText(requireContext(), "Gagal mengubah bio", Toast.LENGTH_SHORT)
                        .show()
                }
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
