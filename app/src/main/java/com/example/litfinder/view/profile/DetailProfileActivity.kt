package com.example.litfinder.view.profile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.litfinder.R
import com.example.litfinder.databinding.ActivityDetailProfileBinding
import com.example.litfinder.databinding.ActivityRegisterBinding
import com.example.litfinder.view.register.RegisterViewModel
import com.example.litfinder.view.viewModelFactory.ViewModelFactory

class DetailProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(PersonalDetailsFragment())
        }


    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun navigateToChangeName() {
        loadFragment(ChangeNameFragment())
    }

    fun navigateToChangeBio() {
        loadFragment(ChangeBioFragment())
    }

    fun navigateToChangePassword() {
        loadFragment(ChangePasswordFragment())
    }


}
