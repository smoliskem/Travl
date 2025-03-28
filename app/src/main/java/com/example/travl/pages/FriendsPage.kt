package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.travl.R
import com.example.travl.databinding.FriendsPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FriendsPage : Fragment() {
    private lateinit var binding: FriendsPageBinding
    //private lateinit var textViewUserName: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.friends_page, container, false)

        auth = Firebase.auth
        //textViewUserName = view.findViewById(R.id.username)

        //val user = FirebaseAuth.getInstance().currentUser
        //val displayName = user?.displayName

        //textViewUserName.text = displayName
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FriendsPageBinding.bind(view)

        binding.backBtn.setOnClickListener {
            findNavController().navigate(FriendsPageDirections.actionFriendsPageToProfilePage())
        }

        binding.friendRequests.setOnClickListener {
            findNavController().navigate(FriendsPageDirections.actionFriendsPageToFriendsRequestsPage())
        }


    }
}