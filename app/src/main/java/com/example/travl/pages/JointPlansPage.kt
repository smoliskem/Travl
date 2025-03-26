package com.example.travl.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.travl.R
import com.example.travl.databinding.JointPlansPageBinding


class JointPlansPage : Fragment() {
    private lateinit var binding: JointPlansPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.joint_plans_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = JointPlansPageBinding.bind(view)

        binding.mainPageBtn.setOnClickListener {
            findNavController().navigate(JointPlansPageDirections.actionJointPlansPageToMainPage())
        }
        binding.profileBtn.setOnClickListener {
            findNavController().navigate(JointPlansPageDirections.actionJointPlansPageToProfilePage())
        }

        binding.myPlansBtn.setOnClickListener {
            findNavController().navigate(JointPlansPageDirections.actionJointPlansPageToMyPlansPage())
        }
    }
}