package com.example.medirem.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medirem.R
import com.example.medirem.data.remote.FirebaseHelper

class GetStartedFragment : Fragment(R.layout.fragment_get_started) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FirebaseHelper.isUserLoggedIn()) {
            findNavController().navigate(R.id.action_getStartedFragment_to_homeFragment)
        }

        view.findViewById<Button>(R.id.btnGetStarted).setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_signupFragment)
        }

        view.findViewById<TextView>(R.id.tvLoginLink).setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_loginFragment)
        }
    }
}