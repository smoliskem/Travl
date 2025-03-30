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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

class EditProfilePage : Fragment() {
    private lateinit var binding: EditProfilePageBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.edit_profile_page, container, false)

        auth = Firebase.auth

        user = auth.currentUser!!

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
            val newName = binding.editName.text.toString().trim()
            val currentPassword = binding.editCurPassword.text.toString()
            val newPassword = binding.editNewPassword.text.toString()
            val curName = user.displayName ?: ""

            coroutineScope.launch {
                try {
                    var nameUpdated = false
                    var passwordUpdated = false
                  
                    if (newName.isNotEmpty()) {
                        if (curName != newName) {
                            if (isUsernameAvailable(newName)) {
                                nameUpdated = updateUsername(user, newName, curName)
                            } else {
                                showToast("Это имя пользователя уже занято")
                            }
                        } else {
                            showToast("Новое имя должно отличаться от старого")
                        }
                    }

                    if (newPassword.isNotEmpty()) {
                        passwordUpdated = handlePasswordChange(currentPassword, newPassword)
                    }

                    if (nameUpdated || passwordUpdated) {
                        navigateToProfile()
                    }
                } catch (e: Exception) {
                    showError(e)
                }
            }
        }
    }

    private suspend fun isUsernameAvailable(username: String): Boolean {
        return try {
            val snapshot = db.collection("usernames")
                .document(username.lowercase(Locale.getDefault()))
                .get()
                .await()
            !snapshot.exists()
        } catch (e: Exception) {
            showError("Ошибка проверки имени пользователя")
            false
        }
    }

    private suspend fun updateUsername(
        user: FirebaseUser?,
        newName: String,
        oldName: String
    ): Boolean {
        return try {
            // 1. Удаляем старое имя из Firestore
            if (oldName.isNotEmpty()) {
                db.collection("usernames")
                    .document(oldName.lowercase(Locale.getDefault()))
                    .delete()
                    .await()
            }

            // 2. Обновляем отображаемое имя в Firebase Auth
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build()
            user?.updateProfile(profileUpdates)?.await()

            // 3. Добавляем новое имя в Firestore
            db.collection("usernames")
                .document(newName.lowercase(Locale.getDefault()))
                .set(mapOf(
                    "userId" to user?.uid,
                    "createdAt" to System.currentTimeMillis()
                ))
                .await()

            showToast("Имя успешно изменено")
            true
        } catch (e: Exception) {
            showError("Ошибка при обновлении имени", e)
            false
        }
    }


    private suspend fun handlePasswordChange(
        currentPassword: String,
        newPassword: String
    ): Boolean {
        return when {
            currentPassword.isEmpty() -> {
                showToast("Введите текущий пароль")
                false
            }
            currentPassword == newPassword -> {
                showToast("Новый пароль должен отличаться от старого")
                false
            }
            else -> changePassword(user, currentPassword, newPassword)
        }
    }
    
    private suspend fun changePassword(
        user: FirebaseUser,
        currentPassword: String,
        newPassword: String
    ): Boolean {
        return try {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).await()
            user.updatePassword(newPassword).await()
            showToast("Пароль успешно изменён")
            true
        } catch (e: Exception) {
            showError("Ошибка изменения пароля", e)
            false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun showError(exception: Exception) {
        showError("Ошибка", exception)
    }

    private fun showError(message: String, exception: Exception? = null) {
        val errorMsg = exception?.message?.let { "$message: $it" } ?: message
        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToProfile() {
        findNavController().navigate(EditProfilePageDirections.actionEditPageToProfilePage())
    }

}