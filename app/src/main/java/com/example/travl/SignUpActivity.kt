package com.example.travl

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travl.databinding.SignUpPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: SignUpPageBinding
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    //Установка слушателей
    private fun setupClickListeners() {
        binding.alreadyHaveAccountButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.createAccountButton.setOnClickListener {
            val username = binding.editTextName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            when {
                !areFieldsValid(
                    username,
                    email,
                    password
                ) -> showToast("Please fill all fields correctly")

                !isValidUsername(username) -> showToast("Username must be 3-10 alphanumeric characters")
                else -> checkAndRegister(username, email, password)
            }
        }
    }

    //Проверка имени пользователя на уникальность и регистрация
    private fun checkAndRegister(username: String, email: String, password: String) {
        checkUsernameUniqueness(username) { isUnique ->
            if (isUnique) {
                registerUser(username, email, password)
            } else {
                showToast("Username is already taken")
            }
        }
    }

    //Регистрация пользователя в Firebase по почте и паролю
    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUsernameAndCompleteRegistration(username)
                } else {
                    showToast("Registration failed: ${task.exception?.message}")
                }
            }
    }

    //Сохранение имени пользователя в Firestore и завершение регистрации
    private fun saveUsernameAndCompleteRegistration(username: String) {
        saveUsername(username) { success ->
            if (success) {
                completeRegistration(auth.currentUser, username)
            } else {
                auth.currentUser?.delete()
                showToast("Failed to reserve username")
            }
        }
    }

    //Отправка верификационного письма и переход на некст страницу
    private fun completeRegistration(user: FirebaseUser?, username: String) {
        user?.apply {
            updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build())
            sendVerificationEmail()
            navigateToSignIn()
        }
    }

    private fun FirebaseUser.sendVerificationEmail() {
        sendEmailVerification().addOnCompleteListener { task ->
            val message = if (task.isSuccessful) {
                "Verification email sent. Please check your email."
            } else {
                "Failed to send verification email."
            }
            showToast(message)
        }
    }

    private fun checkUsernameUniqueness(username: String, callback: (Boolean) -> Unit) {
        db.collection("usernames")
            .document(username.lowercase(Locale.getDefault()))
            .get()
            .addOnSuccessListener { callback(!it.exists()) }
            .addOnFailureListener {
                showToast("Error checking username availability")
                callback(false)
            }
    }

    private fun saveUsername(username: String, callback: (Boolean) -> Unit) {
        val usernameLower = username.lowercase(Locale.getDefault())
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val data = mapOf(
                "userId" to userId,
                "username" to usernameLower
            )

            db.collection("usernames")
                .document(usernameLower)
                .set(data)
                .addOnSuccessListener { callback(true) }
                .addOnFailureListener { callback(false) }
        } else {
            callback(false)
        }
    }

    private fun navigateToSignIn() {
        startActivity(Intent(this, SignInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    private fun areFieldsValid(username: String, email: String, password: String) =
        username.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length >= 6

    private fun isValidUsername(username: String) =
        username.length in 3..10 && username.matches("^[a-zA-Z0-9_]*$".toRegex())

    private fun showToast(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    }
}