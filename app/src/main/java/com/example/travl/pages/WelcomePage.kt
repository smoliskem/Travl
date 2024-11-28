package com.example.travl.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.travl.R


class WelcomePage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.welcome_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signIn = view.findViewById<Button>(R.id.signInBtn)
        val signUp = view.findViewById<Button>(R.id.signUpBtn)
        val controller = findNavController()
        signIn.setOnClickListener { controller.navigate(R.id.signIn) }
        signUp.setOnClickListener { controller.navigate(R.id.signUp) }
    }
}