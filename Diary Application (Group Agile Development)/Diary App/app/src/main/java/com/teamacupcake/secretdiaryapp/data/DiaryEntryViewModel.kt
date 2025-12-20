package com.teamacupcake.secretdiaryapp.data

import android.app.Application
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID


class DiaryEntryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _entries = MutableLiveData<List<DiaryEntry>?>()
    val entries: MutableLiveData<List<DiaryEntry>?> = _entries


    // Counter to force recomposition
    var recompositionCounter = mutableStateOf(0)
        private set

    private val _searchResults = MutableLiveData<List<DiaryEntry>>()
    val searchResults: LiveData<List<DiaryEntry>> = _searchResults

    var locationName by mutableStateOf("")
    var latitude by mutableStateOf<Double?>(null)
    var longitude by mutableStateOf<Double?>(null)
    private val _entryDeleted = MutableLiveData<Boolean>()
    val entryDeleted: LiveData<Boolean> = _entryDeleted


    init {
        Log.d(TAG, "Initializing ViewModel and fetching entries")

        fetchDiaryEntries()
        fetchDiaryEntriesWithImages()
        fetchEntryImagesAndUpdate()
    }


    // Call this method from `onLocationSelected`
    fun setLocationDetails(lat: Double, lon: Double, name: String) {
        latitude = lat
        longitude = lon
        locationName = name
        Log.v("in view model", "Location selected: Lat=$latitude, Lon=$longitude, Name=$locationName")

    }

     fun fetchDiaryEntries() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "Successfully fetched diary entries")

                val querySnapshot = db.collection("diaryEntries")
                    .whereEqualTo("userId", userId)
                    .get().await()
                val entries = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(DiaryEntry::class.java)?.apply {
                        docId = document.id
                        imageUrl = fetchImageUrl(docId).await() ?: ""

                    }
                }
                _entries.postValue(entries)
                // Increment the counter to force recomposition
                recompositionCounter.value++

            } catch (e: Exception) {
                Log.w(TAG, "Error fetching entries", e)
            }
        }
    }


    fun insert(diaryEntry: DiaryEntry) = viewModelScope.launch(Dispatchers.IO) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("diaryEntries")
                .add(diaryEntry.copy(userId = userId))
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    fun deleteDiaryEntry(docId: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("DiaryEntryViewModel", "Attempting to delete entry with docId: $docId")
        try {
            db.collection("diaryEntries").document(docId).delete().await()
            Log.d("DiaryEntryViewModel", "Successfully deleted entry")

            val updatedList = _entries.value?.filterNot { it.docId == docId }
            Log.d("DiaryEntryViewModel", "Updated entries list size: ${updatedList?.size}")

            withContext(Dispatchers.Main) {
                _entries.value = updatedList
                Log.d("DiaryEntryViewModel", "Entries LiveData updated")
            }
        } catch (e: Exception) {
            Log.w("DiaryEntryViewModel", "Error deleting entry", e)
        }
    }




// You don't need a separate function to remove the entry from the list.
// LiveData should be the single source of truth for your UI.


//    fun deleteDiaryEntry(docId: String) = viewModelScope.launch(Dispatchers.IO) {
//
//
//        try {
//
//            db.collection("diaryEntries").document(docId).delete().await()
//            // Update LiveData on the main thread after deletion
//            val updatedList = _entries.value?.filterNot { it.docId == docId }
//            withContext(Dispatchers.Main) {
//                _entries.value = updatedList
//            }
//        } catch (e: Exception) {
//            Log.w(TAG, "Error deleting entry", e)
//        }
//
//    }

    // Use this function to remove an entry from the list
    fun removeEntry(entry: DiaryEntry) {
        _entries.value = _entries.value?.filterNot { it.docId == entry.docId }
    }



    private val _selectedDateEntries = MutableLiveData<List<DiaryEntry>>()
    val selectedDateEntries: LiveData<List<DiaryEntry>> = _selectedDateEntries

    fun fetchEntriesForSelectedDate(date: String) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = db.collection("diaryEntries")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("date", date)
                    .get().await()
                val entries = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(DiaryEntry::class.java)?.apply { docId = document.id }
                }
                _selectedDateEntries.postValue(entries)
            } catch (e: Exception) {
                Log.w(TAG, "Error fetching entries for date", e)
            }
        }
    }
//    fun uploadImage(imageUri: Uri?, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
//        val storageReference = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}.jpg")
//        if (imageUri != null) {
//            storageReference.putFile(imageUri)
//                .addOnSuccessListener { taskSnapshot ->
//                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        val imageUrl = uri.toString()
//                        onSuccess(imageUrl)
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    onError(exception)
//                }
//        }
//    }
    fun uploadImage(imageUri: Uri?, onSuccess: (imageUrl: String, storagePath: String) -> Unit, onError: (Exception) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}.jpg")
        if (imageUri != null) {
            storageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl
                        ?.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val storagePath = storageRef.path
                            onSuccess(imageUrl, storagePath)
                        }
                        ?.addOnFailureListener { exception ->
                            onError(exception)
                        }
                }
                .addOnFailureListener { exception ->
                    onError(exception)
                }
        } else {
            onError(Exception("Image URI must not be null"))
        }
    }


    fun searchDiaryEntries(query: String) {
        // Ensure the query length is at least 3 characters before performing the search
        if (query.length < 2) {
            _searchResults.value = emptyList() // or keep the current list, depending on desired behavior
            return
        }

        _searchResults.value = _entries.value?.filter { diaryEntry ->
            // Use a regular expression to find any sequence of at least 3 letters/numbers
            val pattern = Regex(".*$query.*", RegexOption.IGNORE_CASE)
            pattern.containsMatchIn(diaryEntry.title) || pattern.containsMatchIn(diaryEntry.content)
        }
    }


    private val storageReference = FirebaseStorage.getInstance().reference




//    fun uploadImage(imageUri: Uri?, onSuccess: (Pair<String, String>) -> Unit, onError: (Exception) -> Unit) {
//        val storageRef = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}.jpg")
//        if (imageUri != null) {
//            storageRef.putFile(imageUri)
//                .addOnSuccessListener { taskSnapshot ->
//                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        val imageUrl = uri.toString()
//                        val storagePath = storageRef.path
//                        onSuccess(Pair(imageUrl, storagePath))
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    onError(exception)
//                }
//        }
//    }




    private fun fetchImageUrlForEntry(docId: String, onImageFetched: (String?) -> Unit) {
        // This function assumes that you're fetching the image URL in a manner similar to uploadImage
        // For example, you might have saved the URL in Firestore, or you have a predictable URL pattern in Storage

        Log.d(TAG, "Attempting to fetch image URL for entry: $docId")

        val storageRef = FirebaseStorage.getInstance().reference.child("images/$docId.jpg")
        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                Log.d(TAG, "Successfully fetched image URL for entry $docId")

                onImageFetched(uri.toString())
            }
            .addOnFailureListener {
                // Handle or log failure
                Log.e(TAG, "Error fetching image URL for entry $docId", it)
                onImageFetched(null) // Pass null to indicate failure
            }
    }





    fun fetchEntryImagesAndUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Starting to fetch images for diary entries")
            val entriesSnapshot = _entries.value ?: return@launch
            val fetchImageJobs = entriesSnapshot.map { entry ->
                async {
                    fetchImageUrlForEntry(entry.docId) { imageUrl ->
                        entry.imageUrl = imageUrl
                        Log.d(TAG, "Image URL fetched for entry ${entry.docId}: $imageUrl")
                    }
                }
            }
            fetchImageJobs.awaitAll()
            _entries.postValue(entriesSnapshot)
            Log.d(TAG, "Finished fetching images for diary entries")
            recompositionCounter.value++
        }
    }



    private fun fetchImageUrl(docId: String) = viewModelScope.async(Dispatchers.IO) {
        try {
            val imageUrl = storageReference.child("images/$docId.jpg").downloadUrl.await().toString()
            Log.d(TAG, "Fetched image URL for $docId: $imageUrl")
            imageUrl
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching image URL for $docId", e)
            null
        }
    }



    public fun fetchDiaryEntriesWithImages() {
        Log.d(TAG, "Fetching diary entries with images")

        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = db.collection("diaryEntries")
                    .whereEqualTo("userId", userId)
                    .get().await()
                Log.d(TAG, "Successfully fetched diary entries with images")

                // Assume DiaryEntry class has an 'hasImage' field or similar to check if an image exists
                val entriesWithImages = querySnapshot.documents.map { document ->
                    val entry = document.toObject(DiaryEntry::class.java)
                    entry?.docId = document.id

                    if (entry != null && entry.hasImage) {
                        async {
                            entry.imageUrl = fetchImageUrl(entry.docId).await() ?: ""
                            entry // Return entry with fetched image URL
                        }
                    } else {
                        async { entry } // Return entry without attempting to fetch an image
                    }
                }.awaitAll().filterNotNull()

                withContext(Dispatchers.Main) {
                    _entries.value = entriesWithImages
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error fetching diary entries: ", e)
            }
        }
    }

//    fun searchDiaryEntries(query: String) {
//        // Simple local search implementation
//        _searchResults.value = _entries.value?.filter {
//            it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
//        }
//    }

    // Get the sum of each mood type in a specified month
    private val _entriesForMonth = MutableLiveData<List<DiaryEntry>>()
    val entriesForMonth: LiveData<List<DiaryEntry>> = _entriesForMonth

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMoodSumsForMonth() : Map<String, Int>? {

        val userId = auth.currentUser?.uid ?: return null // check if user exists
        val currentDate = LocalDate.now() // Get the current date
        val currentMonth = currentDate.monthValue.toString().padStart(2, '0') // Get the current month
        val currentYear = currentDate.year // Get the current year
        val firstDayOfMonth = "01"
        val lastDayOfMonth = currentDate.lengthOfMonth()
        var happy = 0
        var sad = 0
        var angry = 0
        var loved = 0
        var shocked = 0
        var data: MutableMap<String, Int> = mutableMapOf(
            Pair("Happy", happy),
            Pair("Sad", sad),
            Pair("Angry", angry),
            Pair("Love", loved),
            Pair("Shocked", shocked)
        )

//        var moodValues = Map
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Query the db to retrieve entries between the first and last day of the current month
                val querySnapshot = db.collection("diaryEntries")
                    .whereEqualTo("userId", userId)
                    .whereGreaterThanOrEqualTo("date", "$currentYear-$currentMonth-$firstDayOfMonth") // Start of the month
                    .whereLessThanOrEqualTo("date", "$currentYear-$currentMonth-$lastDayOfMonth")    // End of the month
                    .get()
                    .await()
                // Map db documents to diary entry objects
                val entriesForCurrentMonth = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(DiaryEntry::class.java)?.apply { docId = document.id }
                }
                // Iterate through each entry and sum mood values
                entriesForCurrentMonth.forEach{ diaryEntry ->
                    when (diaryEntry.mood) {
                        "😀" -> happy++
                        "😞" -> sad++
                        "😠" -> angry++
                        "😍" -> loved++
                        "🤯" -> shocked++
                    }
                }
                // update the data Map<> to counted values
                data["Happy"] = happy
                data["Sad"] = sad
                data["Angry"] = angry
                data["Love"] = loved
                data["Shocked"] = shocked
            } catch (e: Exception) {
                Log.w("GetEntriesForMonth", "Error fetching entries for given current month", e)
            }
        }
        return data
    }
}
