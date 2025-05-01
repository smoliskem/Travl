package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.travl.R
import com.example.travl.databinding.FriendsRequestPageBinding
import com.example.travl.databinding.PlacePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FriendsRequestsPage : Fragment() {
    private lateinit var binding: FriendsRequestPageBinding
    private val uid = Firebase.auth.currentUser?.uid
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FriendsRequestPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FriendsRequestPageBinding.bind(view)

        binding.backBtn.setOnClickListener {
            findNavController().navigate(FriendsRequestsPageDirections.actionFriendsRequestsPageToFriendsPage())
        }
    }
}