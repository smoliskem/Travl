import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travl.items.Friend
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class FriendsViewModel : ViewModel() {
    private val _dataList = MutableLiveData<List<Friend>>()
    val dataList: LiveData<List<Friend>> get() = _dataList

    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser?.uid

    fun removeItem(itemId: String) {
        _dataList.value = _dataList.value?.filter { it.friendUserID != itemId }
    }

    fun loadData() {
        val requests = mutableListOf<Friend>()


        if (uid != null) {
            val requestTask = getRequests(db, uid, requests)

            Tasks.whenAllComplete(requestTask)
                .addOnSuccessListener {
                    _dataList.value = requests
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error loading collections: ", exception)
                }
        }
    }

    private fun getRequests(
        db: FirebaseFirestore,
        uid: String,
        list: MutableList<Friend>
    ) =
        db.collection("users")
            .document(uid)
            .collection("friends")
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val request = document.toObject(Friend::class.java)
                    request?.let { list.add(it) }
                }
                Log.d("FirestoreSuccess", "Loaded ${list.size} items from favorites")
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error loading favorites: ", exception)
            }
}