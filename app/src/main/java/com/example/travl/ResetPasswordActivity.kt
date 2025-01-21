package com.example.travl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travl.databinding.ResetPasswordPageBinding

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ResetPasswordPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResetPasswordPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}