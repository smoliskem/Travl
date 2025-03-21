package com.example.travl.pages

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.travl.R
import com.example.travl.databinding.MainPageBinding
import com.example.travl.databinding.ProfilePageBinding


class ProfilePage : Fragment() {
    private lateinit var binding: ProfilePageBinding
    private lateinit var textViewUserName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_page, container, false)

        textViewUserName = view.findViewById(R.id.username)

        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name", "Default Name")

        textViewUserName.text = userName
        // Inflate the layout for this fragment
        return view
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