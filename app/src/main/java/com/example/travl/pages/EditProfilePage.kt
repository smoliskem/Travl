package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.travl.R
import com.example.travl.databinding.EditProfilePageBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditProfilePage : Fragment() {
    private lateinit var binding: EditProfilePageBinding

    //private lateinit var textViewUserName: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.edit_profile_page, container, false)

        auth = Firebase.auth

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EditProfilePageBinding.bind(view)


        binding.backBtn.setOnClickListener {
            findNavController().navigate(EditProfilePageDirections.actionEditPageToProfilePage())
        }

        binding.acceptBtn.setOnClickListener {
            val editName = binding.editName.text.toString()
            user = auth.currentUser!!

            if (editName.isNotEmpty()) {
                updateUsername(user, editName)
            } else {
                findNavController().navigate(EditProfilePageDirections.actionEditPageToProfilePage())
            }
        }
    }

    //Смена имени
    private fun updateUsername(user: FirebaseUser?, userName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Username saved",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Навигация только после успешного обновления
                    findNavController().navigate(EditProfilePageDirections.actionEditPageToProfilePage())
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Setting username failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


}