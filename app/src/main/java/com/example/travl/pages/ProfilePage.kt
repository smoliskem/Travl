package com.example.travl.pages


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travl.LoginActivity
import com.example.travl.R
import com.example.travl.databinding.ProfilePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ProfilePage : Fragment() {
    private lateinit var binding: ProfilePageBinding
    private lateinit var textViewUserName: TextView
    private lateinit var auth: FirebaseAuth
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val uid = com.google.firebase.Firebase.auth.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.profile_page, container, false)

        auth = Firebase.auth
        textViewUserName = view.findViewById(R.id.username)

        val user = FirebaseAuth.getInstance().currentUser
        val displayName = user?.displayName

        textViewUserName.text = displayName
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

        binding.logOut.setOnClickListener {
            signOut()
        }

        binding.redactor.setOnClickListener{
            findNavController().navigate(ProfilePageDirections.actionProfilePageToEditPage())
        }

        binding.addFriend.setOnClickListener{
            findNavController().navigate(ProfilePageDirections.actionProfilePageToAddFriendPage())
        }

        binding.friends.setOnClickListener {
            findNavController().navigate(ProfilePageDirections.actionProfilePageToFriendsPage())
        }

        if (uid != null) {
            db.collection("usernames")
                .document(uid) // Замените на актуальный идентификатор документа
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("photoUrl")) {
                        val photoUrl = document.getString("photoUrl")
                        if (photoUrl != null) {
                            // Используйте Glide для загрузки изображения в ImageView
                            Glide.with(this)
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
        // Обновите UI или перенаправьте пользователя на экран входа
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}