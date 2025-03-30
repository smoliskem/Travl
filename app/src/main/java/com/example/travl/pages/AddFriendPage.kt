package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.travl.R
import com.example.travl.databinding.AddFriendPageBinding
import com.example.travl.items.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class AddFriendPage : Fragment() {
    private lateinit var binding: AddFriendPageBinding

    //private lateinit var textViewUserName: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.add_friend_page, container, false)

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
        binding = AddFriendPageBinding.bind(view)

        binding.backBtn.setOnClickListener {
            findNavController().navigate(AddFriendPageDirections.actionAddFriendPageToProfilePage())
        }

        binding.acceptBtn.setOnClickListener {
            val name = binding.friendName.text.toString().trim() // Удаляем пробелы

            if (name.isEmpty()) {
                showToast("Введите имя пользователя")
            } else {
                searchUserByName(name)
            }
        }

    }

    private fun searchUserByName(name: String) {
        if (name == auth.currentUser?.displayName.toString()) {
            showToast("Вы не можете добавить в друзья себя")
        } else {
            val db = FirebaseFirestore.getInstance()
            db.collection("usernames")
                .document(name.lowercase(Locale.getDefault()))
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            showToast("Пользователь $name найден: ${user.userId}")
                        }
                    } else {
                        showToast("Пользователь $name не найден")
                    }
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}