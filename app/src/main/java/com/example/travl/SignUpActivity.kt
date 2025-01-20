package com.example.travl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
                                val builder = AlertDialog.Builder(this)
                                builder.setMessage("Please check your e-mail to verify account")
                                builder.setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    cacheEmail(email)
                                    Firebase.auth.signOut()
                                }
                                val dialog = builder.create()
                                dialog.show()
                            } else {
                                showDialog(
                                    verificationTask.exception?.localizedMessage
                                        ?: "Failed to send verification email."
                                )
                            }
                        }?.addOnFailureListener { exception ->
                            showDialog(
                                exception.localizedMessage ?: "Failed to send verification email."
                            )
                        }
                } else {
                    showDialog(task.exception?.localizedMessage ?: "Registration failed.")
                }
            }
    }


    private fun showDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
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

    private fun cacheEmail(email: String) {
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("email", email)
        editor.apply()
    }
}
