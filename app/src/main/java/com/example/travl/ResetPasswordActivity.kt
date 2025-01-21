package com.example.travl

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travl.databinding.ResetPasswordPageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ResetPasswordPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResetPasswordPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetBtn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()

            if (checkEmailField(email)) {
                resetPassword(email)

                val intent = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            } else {
                Toast.makeText(
                    baseContext,
                    "Enter the correct data.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.circleBack.circleBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun resetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Check your email.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    private fun checkEmailField(email: String): Boolean {
        return email != ""
    }
}

