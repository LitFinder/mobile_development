package com.example.litfinder.view.forgotPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.litfinder.R

class NewPasswordFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextNewPassword = view.findViewById<EditText>(R.id.editTextNewPassword)
        val editTextConfirmPassword = view.findViewById<EditText>(R.id.editTextConfirmPassword)
        val buttonChangePassword = view.findViewById<Button>(R.id.buttonChangePassword)

        buttonChangePassword.setOnClickListener {
            val newPassword = editTextNewPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if (newPassword != confirmPassword) {
                // Show error message or toast indicating passwords don't match
                return@setOnClickListener
            }

            if (newPassword.length < 8) {
                // Show error message or toast indicating password length is less than 8 characters
                return@setOnClickListener
            }

            // Handle changing password logic
            // Move to next fragment or finish activity if password is changed successfully
        }
    }
}
