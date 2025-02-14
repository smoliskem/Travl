package com.example.travl.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.travl.R
import com.example.travl.databinding.MyPlansPageBinding


class MyPlansPage : Fragment() {
    private lateinit var binding: MyPlansPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.my_plans_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MyPlansPageBinding.bind(view)
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