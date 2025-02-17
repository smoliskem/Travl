package com.example.travl.items


import android.util.Log
import com.example.travl.adapters.MainPageParentItemAdapter
import com.google.firebase.firestore.FirebaseFirestore

class PlaceCardGenerator {
    private val caucasus = mutableListOf<PlaceCard>()
    private val altai = mutableListOf<PlaceCard>()
    private val ural = mutableListOf<PlaceCard>()
    private val db = FirebaseFirestore.getInstance()

    fun setDocuments(adapter: MainPageParentItemAdapter) {
        loadCaucasusCollection(adapter)
    }

    private fun loadCaucasusCollection(adapter: MainPageParentItemAdapter) {
        db.collection("Caucasus")
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val place = document.toObject(PlaceCard::class.java)
                    place?.let { caucasus.add(place) }
                }
                loadAltaiCollection(adapter)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }

    private fun loadAltaiCollection(adapter: MainPageParentItemAdapter) {
        db.collection("Altai")
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val place = document.toObject(PlaceCard::class.java)
                    place?.let { altai.add(place) }
                }
                loadUralCollection(adapter)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }

    private fun loadUralCollection(adapter: MainPageParentItemAdapter) {
        db.collection("Ural")
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val place = document.toObject(PlaceCard::class.java)
                    place?.let { ural.add(place) }
                }
                adapter.data = listOf(
                    MainPageParentItem(caucasus, "Кавказ"),
                    MainPageParentItem(ural, "Урал"),
                    MainPageParentItem(altai, "Алтай")
                )
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }

    }
}

