package com.example.litfinder.view.editPreference

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.litfinder.databinding.ActivityEditPreferenceBinding
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class EditPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPreferenceBinding
    private val viewModel by viewModels<EditPreferenceViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}