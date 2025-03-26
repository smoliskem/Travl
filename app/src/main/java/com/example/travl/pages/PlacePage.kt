package com.example.travl.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.travl.R
import com.example.travl.databinding.MainPageBinding
import com.example.travl.databinding.PlacePageBinding


class PlacePage : Fragment() {
    private val args: PlacePageArgs by navArgs()
    private lateinit var binding: PlacePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = PlacePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageResURL = args.imageResURL
        val placeName = args.placeName
        val regionName = args.regionName
        val description = args.description

        Glide.with(requireContext())
            .load(imageResURL)
            .centerCrop()
            .into(binding.placeImg)

        binding.placeName.text = placeName
        binding.regionName.text = regionName
        binding.placeDescription.text = description


        binding.backBtn.setOnClickListener {
            findNavController().navigate(PlacePageDirections.actionPlacePageToMainPage())
        }
    }
}