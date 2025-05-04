package com.example.travl.pages


import FriendsViewModel
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.travl.LoginActivity
import com.example.travl.databinding.ProfilePageBinding
import com.example.travl.viewModels.MyPlansPageViewModel
import com.google.firebase.auth.FirebaseAuth


class ProfilePage : Fragment() {
    private lateinit var binding: ProfilePageBinding
    private lateinit var auth: FirebaseAuth
    private val friendsViewModel: FriendsViewModel by viewModels()
    private val plansViewModel: MyPlansPageViewModel by viewModels()
    private val completeCount = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfilePageBinding.inflate(inflater, container, false)

        friendsViewModel.loadData()

        friendsViewModel.friendsCount.observe(viewLifecycleOwner) { count ->
            binding.friendsCount.text = "$count"
        }

        plansViewModel.loadData()

        plansViewModel.plansCount.observe(viewLifecycleOwner) { count ->
            binding.plansCount.text = "$count"
        }

        val user = FirebaseAuth.getInstance().currentUser
        val displayName = user?.displayName

        binding.username.text = displayName
        binding.completeCount.text = completeCount

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfilePageBinding.bind(view)

        with(binding) {
            mainPageBtn.setOnClickListener {
                findNavController().navigate(ProfilePageDirections.actionProfilePageToMainPage())
            }

            myPlansBtn.setOnClickListener {
                findNavController().navigate(ProfilePageDirections.actionProfilePageToMyPlansPage())
            }

            jointPlansBtn.setOnClickListener {
                findNavController().navigate(ProfilePageDirections.actionProfilePageToJointPlansPage())
            }

            logOut.setOnClickListener {
                signOut()
            }

            completeStat.setOnClickListener {
                findNavController().navigate(ProfilePageDirections.actionProfilePageToCompletePlansPage())
            }

            editor.setOnClickListener {
                findNavController().navigate(ProfilePageDirections.actionProfilePageToEditPage())
            }

            addFriend.setOnClickListener {
                findNavController().navigate(ProfilePageDirections.actionProfilePageToAddFriendPage())
            }

            friends.setOnClickListener {
                findNavController().navigate(ProfilePageDirections.actionProfilePageToFriendsPage())
            }

            friendsStat.setOnClickListener {
                findNavController().navigate(ProfilePageDirections.actionProfilePageToFriendsPage())
            }

            plansStat.setOnClickListener {
                findNavController().navigate(ProfilePageDirections.actionProfilePageToMyPlansPage())
            }
        }
    }

    private fun signOut() {
        auth.signOut() // Выход из аккаунта
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}