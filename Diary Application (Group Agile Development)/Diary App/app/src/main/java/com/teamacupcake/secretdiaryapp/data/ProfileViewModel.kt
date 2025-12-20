package com.teamacupcake.secretdiaryapp.data

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.teamacupcake.secretdiaryapp.ui.utils.isOnline
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ProfileViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _userProfile = MutableLiveData<UserProfile>(UserProfile()) // Initialize with default UserProfile
    val userProfile: LiveData<UserProfile> = _userProfile

    //val userEntries = MutableLiveData<List<DiaryEntry>>()


    // Retrieve the current user's ID from FirebaseAuth
    val currentUserId: String?
        get() = auth.currentUser?.uid

    fun fetchUserProfileLiveData(userId: String): LiveData<UserProfile> {
        // This function triggers the fetch and returns LiveData
        fetchUserProfile(userId)
        return userProfile // This is the LiveData you will observe
    }
    // Suspend function version
    suspend fun fetchUserProfileSuspend(userId: String): UserProfile? = suspendCancellableCoroutine { continuation ->
        db.collection("userProfiles").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userProfile = documentSnapshot.toObject(UserProfile::class.java)?.apply {
                        if (userId.isEmpty()) {
                            this.userId = userId
                            // Update Firestore document...
                        }
                        // Firestore update can also be handled outside this block if needed.
                    }
                    continuation.resume(userProfile)
                } else {
                    // Handle document creation or error
                    val newUserProfile = UserProfile(userId = userId, username = "New User")
                    // Update Firestore and then resume
                    db.collection("userProfiles").document(userId).set(newUserProfile)
                        .addOnSuccessListener {
                            continuation.resume(newUserProfile)
                        }
                        .addOnFailureListener { e ->
                            continuation.resumeWithException(e)
                        }
                }
            }
            .addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
    }

    // Non-suspend wrapper
    fun fetchUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                val userProfile = fetchUserProfileSuspend(userId)
                userProfile?.let {
                    _userProfile.postValue(it)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }


    fun updateUsername(userId: String, newUsername: String) {


        val userRef = db.collection("userProfiles").document(userId)
        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Update existing document
                userRef.update("username", newUsername)
                    .addOnSuccessListener { fetchUserProfile(userId) }
                    .addOnFailureListener { e -> /* Handle error */ }
            } else {
                // Create a new document if it doesn't exist
                val newUserProfile = UserProfile(username = newUsername) // Add more fields as necessary
                userRef.set(newUserProfile)
                    .addOnSuccessListener { fetchUserProfile(userId) }
                    .addOnFailureListener { e -> /* Handle error */ }
            }


            }.addOnFailureListener { e ->
            // Handle any errors
        }
    }

    fun changeProfilePicture(userId: String, imageStream: InputStream, fileName: String) {
        // Upload to Firebase Storage
        val storageRef = storage.reference.child("profilePictures/$userId/$fileName")
        val uploadTask = storageRef.putStream(imageStream)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                // Update Firestore with new profile picture URL
                db.collection("userProfiles").document(userId).update("profilePictureUrl", downloadUri.toString())
                    .addOnSuccessListener {
                        // Fetch updated profile to refresh LiveData
                        fetchUserProfile(userId)
                    }
            } else {
                // Handle failure
            }
        }
    }
}

