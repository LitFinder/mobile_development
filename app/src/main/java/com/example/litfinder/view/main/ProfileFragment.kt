package com.example.litfinder.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentProfileBinding
import com.example.litfinder.view.editPreference.EditPreferenceActivity
import com.example.litfinder.view.login.LoginActivity
import com.example.litfinder.view.profile.DetailProfileActivity
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: MainViewModel
    private var isThemeSwitchInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel = ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)

        setupThemeSwitch()
        setupAction()
        setupObservers()
        binding.btnAkun.setOnClickListener { navigateToDetailProfileActivity() }
        binding.btnPreferences.setOnClickListener { navigateToEditPreferenceActivity() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUserData()
    }

    private fun setupThemeSwitch() {
        val switchTheme = binding.switchTheme

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (!isThemeSwitchInitialized) {
                switchTheme.isChecked = isDarkModeActive
                isThemeSwitchInitialized = true
            } else {
                if (isDarkModeActive != switchTheme.isChecked) {
                    switchTheme.isChecked = isDarkModeActive
                }
            }
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isThemeSwitchInitialized) {
                viewModel.saveThemeSetting(isChecked)
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }
    }

    private fun setupObservers() {
        viewModel.userPhotoUrl.observe(viewLifecycleOwner) { photoUrl ->
            if (photoUrl.isEmpty()) {
                Glide.with(requireContext())
                    .load(R.drawable.default_profile_photo)
                    .into(binding.ivPhoto)
            } else {
                Glide.with(requireContext())
                    .load(photoUrl)
                    .into(binding.ivPhoto)
            }
        }

        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvUsername.text = if (name.isEmpty()) "Nama Pengguna" else name
        }

        viewModel.userBio.observe(viewLifecycleOwner) { bio ->
            binding.tvBio.text = if (bio.isEmpty()) "Bio masih kosong" else bio
        }
    }

    private fun navigateToEditPreferenceActivity() {
        val intent = Intent(requireContext(), EditPreferenceActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToDetailProfileActivity() {
        val intent = Intent(requireContext(), DetailProfileActivity::class.java)
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
                Toast.makeText(
                    requireContext(),
                    getString(R.string.logout_success_message),
                    Toast.LENGTH_SHORT
                ).show()
                navigateToLoginScreen()
            }
        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}



