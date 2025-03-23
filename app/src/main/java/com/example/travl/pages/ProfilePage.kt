package com.example.travl.pages


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.travl.LoginActivity
import com.example.travl.R
import com.example.travl.databinding.ProfilePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfilePage : Fragment() {
    private lateinit var binding: ProfilePageBinding
    private lateinit var textViewUserName: TextView
    private lateinit var auth: FirebaseAuth

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

    }

    private fun signOut() {
        auth.signOut() // Выход из аккаунта
        // Обновите UI или перенаправьте пользователя на экран входа
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}