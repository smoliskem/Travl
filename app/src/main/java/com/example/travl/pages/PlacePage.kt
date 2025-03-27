package com.example.travl.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.travl.databinding.PlacePageBinding
import com.example.travl.items.MyPlansItem
import com.example.travl.items.toMap
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class PlacePage : Fragment() {
    private val args: PlacePageArgs by navArgs()
    private lateinit var binding: PlacePageBinding
    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth. currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = PlacePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageResURI = args.imageResURL
        val placeName = args.placeName
        val regionName = args.regionName
        val description = args.description
        val key = args.key

        Glide.with(requireContext())
            .load(imageResURI)
            .centerCrop()
            .into(binding.placeImg)

        binding.placeName.text = placeName
        binding.regionName.text = regionName
        binding.placeDescription.text = description


        binding.addBtn.setOnClickListener {
            if (uid != null) {
                onFavorites(
                    db,
                    MyPlansItem(imageResURI, placeName, regionName, key),
                    uid
                )
            };
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(PlacePageDirections.actionPlacePageToMainPage())
        }
    }

    private fun onFavorites(
        db: FirebaseFirestore,
        favorite: MyPlansItem,
        uid: String
    ) {
        db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(favorite.key)
            .set(favorite.toMap())
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Place added",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }
}

//Delete
//db.collection("users")
//.document(uid)
//.collection("favorites")
//.document(favorite.key)
//.delete(favorite)