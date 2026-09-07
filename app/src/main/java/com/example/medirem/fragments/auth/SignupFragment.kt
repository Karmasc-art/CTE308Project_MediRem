package com.example.medirem.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medirem.R
import com.example.medirem.data.remote.FirebaseHelper

class SignupFragment : Fragment(R.layout.fragment_signup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.etSignupName)
        val etEmail = view.findViewById<EditText>(R.id.etSignupEmail)
        val etPassword = view.findViewById<EditText>(R.id.etSignupPassword)
        val etReEnter = view.findViewById<EditText>(R.id.etReEnterPassword)
        val btnSignup = view.findViewById<Button>(R.id.btnSignup)
        val tvGoToLogin = view.findViewById<TextView>(R.id.tvGoToLogin)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener { findNavController().popBackStack() }

        btnSignup.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val reEnter = etReEnter.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || reEnter.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != reEnter) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseHelper.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = FirebaseHelper.getUserId() ?: return@addOnCompleteListener
                        val userMap = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "createdAt" to System.currentTimeMillis()
                        )
                        
                        FirebaseHelper.firestore.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Firestore Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        tvGoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}