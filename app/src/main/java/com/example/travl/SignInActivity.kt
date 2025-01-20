package com.example.travl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travl.databinding.SignInPageBinding

class SignInActivity: AppCompatActivity() {
    private lateinit var binding: SignInPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}