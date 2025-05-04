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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travl.LoginActivity
import com.example.travl.databinding.ProfilePageBinding
import com.example.travl.viewModels.CompletePlansViewModel
import com.example.travl.viewModels.MyPlansPageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ProfilePage : Fragment() {
    private lateinit var binding: ProfilePageBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val friendsViewModel: FriendsViewModel by viewModels()
    private val plansViewModel: MyPlansPageViewModel by viewModels()
    private val completePlansViewModel: CompletePlansViewModel by viewModels()
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

        completePlansViewModel.loadData()

        completePlansViewModel.completePlans.observe(viewLifecycleOwner) { count ->
            binding.completeCount.text = "$count"
        }

        val user = FirebaseAuth.getInstance().currentUser
        val displayName = user?.displayName

        binding.username.text = displayName

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
        auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}