package com.example.travl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.travl.databinding.SignUpPageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: SignUpPageBinding
    private lateinit var auth: FirebaseAuth

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
            val etName = binding.editTextName.text.toString()
            val etMail = binding.editTextEmail.text.toString()
            val etPassword = binding.editTextPassword.text.toString()
            if (checkFields(etName, etMail, etPassword)) {
                createUserWithEmailAndPassword(etMail, etPassword)
            } else {
                Toast.makeText(
                    baseContext,
                    "Enter the correct data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                Toast.makeText(
                                    baseContext,
                                    "Please check your e-mail to verify account.",
                                    Toast.LENGTH_SHORT,
                                ).show()

                                val intent = Intent(this, SignInActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)

                                Firebase.auth.signOut()
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }?.addOnFailureListener { _ ->
                            Toast.makeText(
                                baseContext,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        baseContext,
                        "Registration failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun checkFields(
        name: String,
        email: String,
        password: String
    ): Boolean {
        return !(fieldIsEmpty(name) || fieldIsEmpty(email) || fieldIsEmpty(password))
    }

    private fun fieldIsEmpty(editText: String): Boolean {
        return editText == ""
    }


}
