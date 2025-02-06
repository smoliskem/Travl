package com.example.travl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.MainPageItemAdapter
import com.example.travl.databinding.MainPageBinding
import com.example.travl.databinding.MyPlansPageBinding
import com.example.travl.items.MainPageItem
import com.example.travl.items.MainPageItemGenerator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainPageBinding
    private lateinit var adapter: MainPageItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = MainPageItemAdapter() // Создание адаптера
        adapter.data = MainPageItemGenerator.generateMainPageItem(10) // Заполнение данными поля data

        binding.mainPageItemRecycler.layoutManager =
            manager // Назначение LayoutManager для RecyclerView
        binding.mainPageItemRecycler.adapter = adapter // Назначение адаптера для RecyclerView
    }
}
