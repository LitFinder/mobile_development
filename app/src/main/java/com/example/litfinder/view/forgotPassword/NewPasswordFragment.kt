package com.example.litfinder.view.forgotPassword

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentNewPasswordBinding
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.view.login.LoginActivity
import com.example.litfinder.view.profile.DetailProfileActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory
import kotlinx.coroutines.launch

class NewPasswordFragment : Fragment() {
    private lateinit var viewModel: ForgotPasswordViewModel
    private var _binding: FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel = ViewModelProvider(requireActivity(), factory).get(ForgotPasswordViewModel::class.java)

        binding.buttonChangePassword.setOnClickListener {
            val newPassword = binding.editTextNewPassword.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()

            if (newPassword != confirmPassword) {
                Toast.makeText(requireContext(), R.string.password_mismatch_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword.length < 8) {
                Toast.makeText(requireContext(), R.string.password_length_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.changePassword(newPassword)
        }

        viewModel.passwordChangeStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ApiResponseStatus.Loading -> {
                    // Show loading indicator if needed
                }

                is ApiResponseStatus.Success -> {
                    if (status.data) {
                        Toast.makeText(
                            requireContext(),
                            R.string.password_changed_successfully,
                            Toast.LENGTH_SHORT
                        ).show()
                        checkTokenAndNavigate()
                    }
                }

                is ApiResponseStatus.Error -> {
                    Toast.makeText(requireContext(), status.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkTokenAndNavigate() {
        lifecycleScope.launch {
            val token = viewModel.getToken()
            if (token.isNullOrEmpty()) {
                navigateToLogin()
            } else {
                navigateToPersonalDetails()
            }
        }
    }

    private fun navigateToPersonalDetails() {
        if (requireActivity() is DetailProfileActivity) {
            (requireActivity() as DetailProfileActivity).navigateToPersonalDetails()
        } else {
            val intent = Intent(requireContext(), DetailProfileActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
