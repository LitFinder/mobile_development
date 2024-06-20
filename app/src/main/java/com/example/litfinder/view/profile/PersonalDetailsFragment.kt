package com.example.litfinder.view.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.litfinder.R
import com.example.litfinder.databinding.FragmentPersonalDetailsBinding
import com.example.litfinder.view.viewModelFactory.ViewModelFactory
import java.io.File

class PersonalDetailsFragment : Fragment() {
    private var _binding: FragmentPersonalDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailProfileViewModel
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireActivity().applicationContext
        val factory = ViewModelFactory(context)
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(DetailProfileViewModel::class.java)

        binding.changeName.setOnClickListener {
            (activity as DetailProfileActivity).navigateToChangeName()
        }

        binding.changeBio.setOnClickListener {
            (activity as DetailProfileActivity).navigateToChangeBio()
        }

        binding.changePassword.setOnClickListener {
            (activity as DetailProfileActivity).navigateToChangePassword()
        }

        binding.backToProfile.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        setupObservers()

        binding.ivPhoto.setOnClickListener {
            openGallery()
        }

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
            binding.tvName.text = name
        }

        viewModel.userBio.observe(viewLifecycleOwner) { bio ->
            if (bio.isNullOrEmpty()) {
                binding.tvBio.text = "Bio masih kosong"
                binding.tvBio.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey
                    )
                )
            } else {
                binding.tvBio.text = bio
                binding.tvBio.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
        }

        viewModel.navigateToDetailProfile.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(requireContext(), DetailProfileActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
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

        viewModel.updatePictureResponse.observe(viewLifecycleOwner) { response ->
            if (response.status == "success") {
                Toast.makeText(
                    requireContext(),
                    "Profile picture updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Failed to update profile picture",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            openGalleryInternal()
        }
    }

    private fun openGalleryInternal() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryInternal()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Izin akses ke galeri ditolak",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = requireActivity().contentResolver.query(
                selectedImageUri!!,
                filePathColumn,
                null,
                null,
                null
            )
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            val picturePath = columnIndex?.let { cursor.getString(it) }
            cursor?.close()
            val file = File(picturePath!!)
            viewModel.updateProfilePicture(file)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUserData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val PERMISSION_REQUEST_CODE = 2
    }

}
