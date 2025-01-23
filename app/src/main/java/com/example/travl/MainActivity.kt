package com.example.travl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travl.databinding.MyPlansPageBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MyPlansPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyPlansPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
