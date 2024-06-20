package com.example.litfinder.view.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.litfinder.databinding.FragmentForgotPasswordBinding
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(ForgotPasswordViewModel::class.java)

        binding.buttonSendCode.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            if (email.isNotEmpty()) {

                viewModel.sendVerificationCode(email)
            } else {
                // Handle empty email
            }
        }

        viewModel.verificationCodeSent.observe(viewLifecycleOwner) { sent ->
            if (sent) {
                (activity as ForgotPasswordActivity).navigateToVerificationCode()
            } else {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
