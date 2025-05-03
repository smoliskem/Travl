package com.example.travl

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travl.databinding.WelcomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: WelcomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация Firebase Auth перед setContentView
        auth = Firebase.auth

        // Проверка авторизации сразу при создании активности
        if (auth.currentUser != null) {
            redirectToMain()
            return  // Прерываем дальнейшее выполнение onCreate
        }

        binding = WelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        // Дополнительная проверка при возобновлении активности
        if (auth.currentUser != null) {
            redirectToMain()
        }
    }

    private fun redirectToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()  // Закрываем LoginActivity чтобы нельзя было вернуться назад
    }
}