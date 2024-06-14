package com.example.litfinder.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentProfileBinding
import com.example.litfinder.remote.api.User
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.remote.pref.dataStore
import com.example.litfinder.view.login.LoginActivity
import com.example.litfinder.view.profile.DetailProfileActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mendapatkan context dari activity
        val context = requireActivity().applicationContext

        // Inisialisasi ViewModelFactory
        val factory = ViewModelFactory(context)

        // Mendapatkan instance MainViewModel menggunakan ViewModelProvider
        viewModel = ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)

        setupAction()
        binding.btnAkun.setOnClickListener { navigateToDetailProfileActivity() }
    }

    private fun navigateToDetailProfileActivity() {
        val intent = Intent(requireContext(), DetailProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setupAction() {
        binding.btnKeluar.setOnClickListener {
            viewModel.logout()
        }
        observeLogout()
    }

    private fun observeLogout() {
        viewModel.logoutResult.observe(viewLifecycleOwner) { isLoggedOut ->
            if (isLoggedOut) {
                Toast.makeText(requireContext(), getString(R.string.logout_success_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
