package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travl.adapters.MyPlansPageItemAdapter
import com.example.travl.databinding.MyPlansPageBinding



class MyPlansPage : Fragment() {
    private lateinit var binding: MyPlansPageBinding
    private lateinit var adapter: MyPlansPageItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = MyPlansPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MyPlansPageItemAdapter(requireContext())


        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.myPlansRecycler.layoutManager =
            manager


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