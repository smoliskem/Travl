package com.example.travl.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.travl.R
import com.example.travl.databinding.MainPageBinding
import com.example.travl.databinding.ProfilePageBinding


class ProfilePage : Fragment() {
    private lateinit var binding: ProfilePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfilePageBinding.bind(view)

        binding.mainPageBtn.setOnClickListener {
            findNavController().navigate(ProfilePageDirections.actionProfilePageToMainPage())
        }
        binding.myPlansBtn.setOnClickListener {
            findNavController().navigate(ProfilePageDirections.actionProfilePageToMyPlansPage())
        }
        binding.jointPlansBtn.setOnClickListener {
            findNavController().navigate(ProfilePageDirections.actionProfilePageToJointPlansPage())
        }

    }
}