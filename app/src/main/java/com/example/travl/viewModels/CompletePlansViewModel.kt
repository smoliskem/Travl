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

class CompletePlansViewModel : ViewModel() {
    private val _dataList = MutableLiveData<List<MyPlansItem>>()
    val dataList: LiveData<List<MyPlansItem>> get() = _dataList

    private val _completePlans = MutableLiveData<Int>()
    val completePlans: LiveData<Int> get() = _completePlans

    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser?.uid

    fun removeItem(itemId: String) {
        _dataList.value = _dataList.value?.filter { it.key != itemId }
    }

    fun loadData() {
        val completes = mutableListOf<MyPlansItem>()


        if (uid != null) {
            val completeTask = getCompletes(db, uid, completes)

            Tasks.whenAllComplete(completeTask)
                .addOnSuccessListener {
                    _dataList.value = completes
                    _completePlans.value = completes.size
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error loading collections: ", exception)
                    _completePlans.value = 0
                }
        } else {
            _completePlans.value = 0
        }
    }

    private fun getCompletes(
        db: FirebaseFirestore,
        uid: String,
        list: MutableList<MyPlansItem>
    ) =
        db.collection("users")
            .document(uid)
            .collection("complete")
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val place = document.toObject(MyPlansItem::class.java)
                    place?.let { list.add(it) }
                }
                Log.d("FirestoreSuccess", "Loaded ${list.size} items from completes")
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error loading completes: ", exception)
            }
}