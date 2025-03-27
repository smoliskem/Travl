package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.MyPlansPageItemAdapter
import com.example.travl.databinding.MyPlansPageBinding
import com.example.travl.viewModels.MyPlansPageViewModel


class MyPlansPage : Fragment() {
    private lateinit var binding: MyPlansPageBinding
    private lateinit var adapter: MyPlansPageItemAdapter
    private val viewModel: MyPlansPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = MyPlansPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Инициализация адаптера
        adapter = MyPlansPageItemAdapter(requireContext())


        //Менеджер RecyclerView
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Присваивание менеджера
        binding.myPlansRecycler.layoutManager =
            manager


        //Наблюдатель
        viewModel.dataList.observe(viewLifecycleOwner) { data ->
            adapter.data = data
        }

        //Асинхронное обновление данных
        viewModel.loadData()

        //Присваивание адаптера
        binding.myPlansRecycler.adapter = adapter


        val controller = findNavController()

        binding.mainPageBtn.setOnClickListener {
            controller.navigate(MyPlansPageDirections.actionMyPlansPageToMainPage())
        }
        binding.jointPlansBtn.setOnClickListener {
            controller.navigate(MyPlansPageDirections.actionMyPlansPageToJointPlansPage())
        }
        binding.profileBtn.setOnClickListener {
            controller.navigate(MyPlansPageDirections.actionMyPlansPageToProfilePage())
        }
    }
}