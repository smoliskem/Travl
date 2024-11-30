package com.example.travl.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.travl.R


class ProfilePage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainBtn = view.findViewById<ImageButton>(R.id.mainPageBtn)
        val myBtn = view.findViewById<ImageButton>(R.id.plansBtn)
        val withBtn = view.findViewById<ImageButton>(R.id.withBtn)
        val controller = findNavController()
        mainBtn.setOnClickListener { controller.navigate(R.id.mainPage) }
        myBtn.setOnClickListener { controller.navigate(R.id.myPlansPage) }
        withBtn.setOnClickListener { controller.navigate(R.id.withPlansPage) }
    }
}