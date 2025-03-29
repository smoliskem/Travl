package com.example.travl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travl.databinding.SignUpPageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: SignUpPageBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.alreadyHaveAccountButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.createAccountButton.setOnClickListener {
            val username = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (checkFields(username, email, password)) {
                checkUsernameUniqueness(username) { isUnique ->
                    if (isUnique) {
                        createUserWithEmailAndPassword(email, password, username)
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Username is already taken",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Please fill all fields correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkUsernameUniqueness(username: String, callback: (Boolean) -> Unit) {
        db.collection("usernames")
            .document(username)
            .get()
            .addOnSuccessListener { document ->
                callback(!document.exists())
            }
            .addOnFailureListener { e ->
                Log.e("SignUp", "Error checking username", e)
                Toast.makeText(
                    baseContext,
                    "Error checking username availability",
                    Toast.LENGTH_SHORT
                ).show()
                callback(false)
            }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Сохраняем имя пользователя в Firestore
                    saveUsername(username) { success ->
                        if (success) {
                            // Обновляем профиль и отправляем email верификации
                            completeRegistration(auth.currentUser, username)
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Failed to reserve username",
                                Toast.LENGTH_SHORT
                            ).show()
                            auth.currentUser?.delete()
                        }
                    }
                } else {
                    Toast.makeText(
                        baseContext,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun saveUsername(username: String, callback: (Boolean) -> Unit) {
        val userData = hashMapOf(
            "userId" to auth.currentUser?.uid,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("usernames")
            .document(username)
            .set(userData)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("SignUp", "Error saving username", e)
                callback(false)
            }
    }

    private fun completeRegistration(user: FirebaseUser?, username: String) {
        // 1. Обновляем профиль
        updateUserProfile(user, username)

        // 2. Отправляем email верификации
        user?.sendEmailVerification()
            ?.addOnCompleteListener { verificationTask ->
                if (verificationTask.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Verification email sent. Please check your email.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // 3. Переходим на экран входа
                    navigateToSignIn()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUserProfile(user: FirebaseUser?, username: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("SignUp", "Failed to update profile", task.exception)
                }
            }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun checkFields(
        name: String,
        email: String,
        password: String
    ): Boolean {
        return name.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length >= 6
    }
}