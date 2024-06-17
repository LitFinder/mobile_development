package com.example.litfinder.view.forgotPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentForgotPasswordBinding
import com.example.litfinder.databinding.FragmentVerificationCodeBinding
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class VerificationCodeFragment : Fragment() {
    private var _binding: FragmentVerificationCodeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerificationCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel = ViewModelProvider(requireActivity(), factory).get(ForgotPasswordViewModel::class.java)

        binding.buttonVerifyCode.setOnClickListener {
            val verificationCode = binding.editTextVerificationCode.text.toString()
            viewModel.verifyCode(verificationCode)
        }

        viewModel.codeVerified.observe(viewLifecycleOwner) { verified ->
            if (verified) {
                Toast.makeText(
                    requireContext(),
                    "Berhasil",
                    Toast.LENGTH_SHORT
                ).show()
                (activity as ForgotPasswordActivity).navigateToNewPassword()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Incorrect verification code. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
