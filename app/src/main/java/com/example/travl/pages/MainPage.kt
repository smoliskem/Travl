package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.R
import com.example.travl.adapters.MainPageParentItemAdapter
import com.example.travl.databinding.MainPageBinding
import com.example.travl.items.MainPageItemGenerator
import com.example.travl.items.MainPageParentItem


class MainPage : Fragment() {
    private lateinit var binding: MainPageBinding
    private lateinit var adapter: MainPageParentItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainPageBinding.bind(view)


        val innerItems1 = MainPageItemGenerator.generateMainPageItem(5)
        val innerItems2 = MainPageItemGenerator.generateMainPageItem(8)
        val innerItems3 = MainPageItemGenerator.generateMainPageItem(3)
        val outerItem = listOf(
            MainPageParentItem(innerItems2, "Регион 1"),
            MainPageParentItem(innerItems1, "Регион 2"),
            MainPageParentItem(innerItems3, "Регион 3")
        )

        adapter = MainPageParentItemAdapter()
        adapter.data = outerItem

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        binding.mainPageRecycler.layoutManager =
            manager // Назначение LayoutManager для RecyclerView
        binding.mainPageRecycler.adapter = adapter // Назначение адаптера для RecyclerView

        binding.myPlansBtn.setOnClickListener {
            findNavController().navigate(MainPageDirections.actionMainPageToMyPlansPage())
        }

        binding.jointPlansBtn.setOnClickListener {
            findNavController().navigate(MainPageDirections.actionMainPageToJointPlansPage())
        }

        binding.profileBtn.setOnClickListener {
            findNavController().navigate(MainPageDirections.actionMainPageToProfilePage())
        }
    }
}