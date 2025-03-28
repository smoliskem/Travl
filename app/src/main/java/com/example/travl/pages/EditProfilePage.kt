package com.example.travl.pages

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditProfilePage : Fragment() {
    private lateinit var binding: EditProfilePageBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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
            val name = binding.editName.text.toString()
            val currentPassword = binding.editCurPassword.text.toString()
            val newPassword = binding.editNewPassword.text.toString()

            user = auth.currentUser!!

            coroutineScope.launch {
                try {
                    var nameUpdated = false
                    var passwordUpdated = false

                    if (name.isNotEmpty()) {
                        nameUpdated = updateUsername(user, name)
                    }

                    if (newPassword.isNotEmpty()) {
                        if (currentPassword.isNotEmpty()) {
                            if (checkPasswords(currentPassword, newPassword)) {
                                passwordUpdated = changePassword(user, currentPassword, newPassword)
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Новый пароль должен отличаться от старого",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Введите текущий пароль",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    if (nameUpdated || passwordUpdated) {
                        withContext(Dispatchers.Main) {
                            findNavController().navigate(EditProfilePageDirections.actionEditPageToProfilePage())
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private suspend fun updateUsername(user: FirebaseUser?, userName: String): Boolean {
        return try {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build()

            user?.updateProfile(profileUpdates)?.await()

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "Имя изменено",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "Ошибка изменения имени: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            false
        }
    }

    private suspend fun changePassword(user: FirebaseUser, currentPassword: String, newPassword: String): Boolean {
        return try {
            // 1. Реаутентификация
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).await()

            // 2. Обновление пароля
            user.updatePassword(newPassword).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "Пароль успешно изменен",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "Ошибка изменения пароля: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            false
        }
    }

    private fun checkPasswords(currentPassword: String, newPassword: String): Boolean {
        return currentPassword != newPassword
    }

}