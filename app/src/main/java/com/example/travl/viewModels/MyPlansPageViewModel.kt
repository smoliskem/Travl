package com.example.travl.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travl.items.MyPlansItem
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MyPlansPageViewModel : ViewModel() {
    private val _dataList = MutableLiveData<List<MyPlansItem>>()
    val dataList: LiveData<List<MyPlansItem>> get() = _dataList

    private val _plansCount = MutableLiveData<Int>()
    val plansCount: LiveData<Int> get() = _plansCount

    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser?.uid

    fun removeItem(itemId: String) {
        _dataList.value = _dataList.value?.filter { it.key != itemId }
    }

    fun loadData() {
        val favorites = mutableListOf<MyPlansItem>()


        if (uid != null) {
            val favoriteTask = getFavorites(db, uid, favorites)

            Tasks.whenAllComplete(favoriteTask)
                .addOnSuccessListener {
                    _dataList.value = favorites
                    _plansCount.value = favorites.size
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error loading collections: ", exception)
                    _plansCount.value = 0
                }
        } else {
            _plansCount.value = 0
        }
    }

    private fun getFavorites(
        db: FirebaseFirestore,
        uid: String,
        list: MutableList<MyPlansItem>
    ) =
        db.collection("users")
            .document(uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val place = document.toObject(MyPlansItem::class.java)
                    place?.let { list.add(it) }
                }
                Log.d("FirestoreSuccess", "Loaded ${list.size} items from favorites")
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error loading favorites: ", exception)
            }
}