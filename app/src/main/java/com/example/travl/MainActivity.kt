package com.example.travl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.MainPageChildItemAdapter
import com.example.travl.adapters.MainPageParentItemAdapter
import com.example.travl.databinding.MainPageBinding
import com.example.travl.items.MainPageItemGenerator
import com.example.travl.items.MainPageParentItem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainPageBinding
    private lateinit var adapter: MainPageParentItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val innerItems1 = MainPageItemGenerator.generateMainPageItem(10)
        val innerItems2 = MainPageItemGenerator.generateMainPageItem(10)
        val outerItem = listOf(
            MainPageParentItem(innerItems2, "Регион 1"),
            MainPageParentItem(innerItems1, "Регион 2")
        )

        adapter = MainPageParentItemAdapter()
        adapter.data = outerItem

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        binding.mainPageRecycler.layoutManager =
            manager // Назначение LayoutManager для RecyclerView
        binding.mainPageRecycler.adapter = adapter // Назначение адаптера для RecyclerView
    }
}
