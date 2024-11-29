package com.example.travl.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.travl.R


class SignIn : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_in_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val forgetBtn = view.findViewById<Button>(R.id.forget_password)
        val regBtn = view.findViewById<Button>(R.id.sign_in)
        val controller = findNavController()
        forgetBtn.setOnClickListener { controller.navigate(R.id.resetPage) }
        regBtn.setOnClickListener { controller.navigate(R.id.signUp) }
    }
}