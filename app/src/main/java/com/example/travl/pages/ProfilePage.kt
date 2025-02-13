package com.example.travl.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val mainBtn = view.findViewById<ImageButton>(R.id.main_page_btn)
        val myBtn = view.findViewById<ImageButton>(R.id.my_plans_btn)
        val withBtn = view.findViewById<ImageButton>(R.id.joint_plans_btn)
        val controller = findNavController()
        mainBtn.setOnClickListener { controller.navigate(R.id.main_page) }
        myBtn.setOnClickListener { controller.navigate(R.id.my_plans_page) }
        withBtn.setOnClickListener { controller.navigate(R.id.joint_plans) }
    }
}