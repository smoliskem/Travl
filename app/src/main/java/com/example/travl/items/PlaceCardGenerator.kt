package com.example.travl.items


import android.util.Log
import com.example.travl.adapters.MainPageParentItemAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object PlaceCardGenerator {

    fun setDocuments(adapter: MainPageParentItemAdapter) {
        val db = Firebase.firestore
        val innerItems1 = mutableListOf<PlaceCard>()

        db.collection("Caucasus")
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val place = document.toObject(PlaceCard::class.java)
                    place?.let { innerItems1.add(place) }
                }

                val outerItem = listOf(
                    MainPageParentItem(innerItems1, "Кавказ"),
                    MainPageParentItem(innerItems1, "Кавказ"),
                    MainPageParentItem(innerItems1, "Кавказ")
                )

                adapter.data = outerItem
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }
}