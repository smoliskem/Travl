package com.example.travl.viewModels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travl.items.MainPageParentItem
import com.example.travl.items.PlaceCard
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
//import androidx.appcompat.app.AppCompatActivity

class MainPageViewModel : ViewModel() {
    private val _dataList = MutableLiveData<List<MainPageParentItem>>()
    val dataList: LiveData<List<MainPageParentItem>> get() = _dataList

    private val db = FirebaseFirestore.getInstance()

    fun loadData() {
        val caucasus = mutableListOf<PlaceCard>()
        val altai = mutableListOf<PlaceCard>()
        val ural = mutableListOf<PlaceCard>()
        val karelia = mutableListOf<PlaceCard>()
        val yakutia = mutableListOf<PlaceCard>()
        val theFarEast = mutableListOf<PlaceCard>()


        val caucasusTask = loadCollection(db, "Caucasus", caucasus)
        val altaiTask = loadCollection(db, "Altai", altai)
        val uralTask = loadCollection(db, "Ural", ural)
        val kareliaTask = loadCollection(db, "Karelia", karelia)
        val yakutiaTask = loadCollection(db, "Yakutia", yakutia)
        val theFarEastTask = loadCollection(db, "The Far East", theFarEast)

        Tasks.whenAllComplete(caucasusTask, altaiTask, uralTask, kareliaTask, yakutiaTask, theFarEastTask)
            .addOnSuccessListener {
                _dataList.value = listOf(
                    MainPageParentItem(caucasus, "Кавказ"),
                    MainPageParentItem(ural, "Урал"),
                    MainPageParentItem(altai, "Алтай"),
                    MainPageParentItem(karelia, "Карелия"),
                    MainPageParentItem(yakutia, "Якутия"),
                    MainPageParentItem(theFarEast, "Дальний Восток")
                )
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error loading collections: ", exception)
            }
    }

    private fun loadCollection(
        db: FirebaseFirestore,
        collectionName: String,
        list: MutableList<PlaceCard>
    ) =
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val place = document.toObject(PlaceCard::class.java)
                    place?.let { list.add(it) }
                }
                Log.d("FirestoreSuccess", "Loaded ${list.size} items from $collectionName")
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error loading $collectionName: ", exception)
            }
}