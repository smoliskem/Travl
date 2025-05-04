package com.example.travl.pages

import com.example.travl.viewModels.FriendsViewModel
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travl.LoginActivity
import com.example.travl.databinding.ProfilePageBinding
import com.example.travl.viewModels.MyPlansPageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ProfilePage : Fragment() {
    private lateinit var binding: ProfilePageBinding
    private val auth = Firebase.auth
    private val friendsViewModel: FriendsViewModel by viewModels()
    private val plansViewModel: MyPlansPageViewModel by viewModels()
    private val completeCount = "0"
    private val db = FirebaseFirestore.getInstance()
    private val uid = com.google.firebase.Firebase.auth.currentUser?.uid

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

            redactor.setOnClickListener {
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

        if (uid != null) {
            db.collection("usernames")
                .document(auth.currentUser?.displayName.toString()) // Замените на актуальный идентификатор документа
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("photoUrl")) {
                        val photoUrl = document.getString("photoUrl")
                        if (photoUrl != null) {
                            // Используйте Glide для загрузки изображения в ImageView
                            Glide.with(requireContext())
                                .load(photoUrl)
                                .apply(RequestOptions().fitCenter())
                                .into(binding.imageView) // Замените на ваш ImageView
                        }
                    }
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