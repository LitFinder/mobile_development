package com.example.litfinder.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentChangePasswordBinding
import com.example.litfinder.view.forgotPassword.ForgotPasswordActivity
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
        viewModel = ViewModelProvider(requireActivity(), factory).get(DetailProfileViewModel::class.java)

        binding.back.setOnClickListener {
            (activity as DetailProfileActivity).navigateToPersonalDetails()
        }

        binding.buttonSavePassword.setOnClickListener {
            val currentPassword = binding.editTextCurrentPassword.text.toString()
            val newPassword = binding.editTextNewPassword.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()

            if (newPassword != confirmPassword) {
                binding.editTextConfirmPassword.error = getString(R.string.password_mismatch_message)
                return@setOnClickListener
            }

            if (newPassword.length < 8) {
                binding.editTextNewPassword.error = getString(R.string.password_length_message)
                return@setOnClickListener
            }

            viewModel.changePassword(currentPassword, newPassword)
        }

        binding.btnForgotPassword.setOnClickListener {
            val intent = Intent(requireContext(), ForgotPasswordActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.changePasswordResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show()
                (activity as DetailProfileActivity).navigateToPersonalDetails()
            }.onFailure {
                if (it.message == "Password Anda sudah sama seperti password sebelumnya") {
                    Toast.makeText(requireContext(), getString(R.string.password_already_same), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
