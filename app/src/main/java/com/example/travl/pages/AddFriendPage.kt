package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.travl.databinding.AddFriendPageBinding
import com.example.travl.items.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class AddFriendPage : Fragment() {
    private lateinit var binding: AddFriendPageBinding
    private val uid = Firebase.auth.currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        binding = AddFriendPageBinding.inflate(inflater, container, false)

        return binding.root
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
                searchUserByName(name, db)
            }
        }

    }

    private fun searchUserByName(
        name: String,
        db: FirebaseFirestore
    ) {
        if (name == auth.currentUser?.displayName.toString()) {
            showToast("Вы не можете добавить в друзья себя")
        } else {
            db.collection("usernames")
                .document(name.lowercase(Locale.getDefault()))
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            sendFriendRequest(user.userId)
                        }
                    } else {
                        showToast("Пользователь $name не найден")
                    }
                }
        }
    }


    private fun sendFriendRequest(friendUserId: String) {
        // Проверяем, не отправили ли мы уже заявку этому пользователю
        if (uid != null) {
            db.collection("users").document(friendUserId)
                .collection("friendRequests")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        showToast("Вы уже отправили заявку этому пользователю")
                    } else {
                        val requestData = hashMapOf(
                            "fromUsername" to auth.currentUser!!.displayName,
                            "fromUserID" to uid,
                            "status" to "pending"
                        )

                        db.collection("users").document(friendUserId)
                            .collection("friendRequests")
                            .document(uid)
                            .set(requestData)
                            .addOnSuccessListener {
                                showToast("Заявка в друзья отправлена")
                            }
                            .addOnFailureListener {
                                showToast("Не удалось отправить заявку")
                            }
                    }
                }
                .addOnFailureListener {
                    showToast("Ошибка при проверке заявки")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}