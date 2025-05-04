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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
//import androidx.appcompat.app.AppCompatActivity

class JointFriendViewModel : ViewModel() {
    private val _dataList = MutableLiveData<List<MyPlansItem>>()
    val dataList: LiveData<List<MyPlansItem>> get() = _dataList
    private val _selectedFriendId = MutableStateFlow<String?>(null)
    val selectedFriendId: StateFlow<String?> = _selectedFriendId.asStateFlow()

    fun setSelectedFriendId(friendId: String) {
        _selectedFriendId.value = friendId
    }

    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser?.uid

    fun removeItem(itemId: String) {
        _dataList.value = _dataList.value?.filter { it.key != itemId }
    }

    fun loadData() {
        val favorites = mutableListOf<MyPlansItem>()


        if (uid != null) {
            val favoriteTask = getFavorites(db, uid, favorites, _selectedFriendId.value.toString())

            Tasks.whenAllComplete(favoriteTask)
                .addOnSuccessListener {
                    _dataList.value = favorites
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error loading collections: ", exception)
                }
        }
    }

    private fun getFavorites(
        db: FirebaseFirestore,
        uid: String,
        list: MutableList<MyPlansItem>,
        friendUserID: String
    ) =
        db.collection("users")
            .document(uid)
            .collection("friends")
            .document(friendUserID)
            .collection("jointPlans")
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