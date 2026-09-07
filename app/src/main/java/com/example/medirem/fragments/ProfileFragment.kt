package com.example.medirem.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medirem.R
import com.example.medirem.data.remote.FirebaseHelper

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName = view.findViewById<TextView>(R.id.tvUserName)
        val tvEmail = view.findViewById<TextView>(R.id.tvUserEmail)

        // Load user data from Firestore
        val userId = FirebaseHelper.getUserId()
        if (userId != null) {
            FirebaseHelper.firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: "User"
                        val email = document.getString("email") ?: ""
                        tvName.text = name
                        tvEmail.text = email
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
        }

        view.findViewById<CardView>(R.id.btnLogoutCard).setOnClickListener {
            FirebaseHelper.auth.signOut()
            findNavController().navigate(R.id.loginFragment)
        }

        view.findViewById<CardView>(R.id.btnNotificationSettings).setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_notificationSettingsFragment)
        }
    }
}