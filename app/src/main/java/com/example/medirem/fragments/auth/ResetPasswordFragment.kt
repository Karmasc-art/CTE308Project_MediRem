package com.example.medirem.fragments.auth

// This fragment is no longer used as the app now uses the standard Firebase reset link flow.
/*
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medirem.R
import com.example.medirem.data.remote.FirebaseHelper

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etCode = view.findViewById<EditText>(R.id.etResetCode)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)
        val btnReset = view.findViewById<Button>(R.id.btnResetPassword)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener { findNavController().popBackStack() }

        btnReset.setOnClickListener {
            val code = etCode.text.toString().trim()
            val newPassword = etNewPassword.text.toString().trim()

            if (code.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword.length < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseHelper.auth.confirmPasswordReset(code, newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Password reset successful! Please login.", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                    } else {
                        Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
*/