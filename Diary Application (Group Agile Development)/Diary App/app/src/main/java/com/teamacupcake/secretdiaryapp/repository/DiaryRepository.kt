package com.teamacupcake.secretdiaryapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.teamacupcake.secretdiaryapp.data.DiaryEntry
import kotlinx.coroutines.tasks.await


class DiaryRepository {

    private val firestoreDB = FirebaseFirestore.getInstance()


    suspend fun getEntriesByFriendId(friendId: String): List<DiaryEntry> {
        return try {
            val documents = firestoreDB.collection("diaryEntries")
                .whereEqualTo("userId", friendId)
                .whereEqualTo("private", false) // Add this line to exclude private entries
                .get()
                .await()
            documents.mapNotNull { it.toObject(DiaryEntry::class.java) }
        } catch (e: Exception) {
            // Log error, handle exception, or return empty list
            emptyList<DiaryEntry>()
        }
    }



    fun getDiaryEntries(onResult: (List<DiaryEntry>) -> Unit) {
        firestoreDB.collection("diary_entries")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle the error
                    onResult(emptyList())
                    return@addSnapshotListener
                }

                val diaryEntryList = snapshot?.documents?.mapNotNull { it.toObject(DiaryEntry::class.java) }
                onResult(diaryEntryList ?: emptyList())
            }
    }

    suspend fun checkFriendship(currentUserId: String?, friendId: String): Boolean {
        val friendshipsCollection = firestoreDB.collection("friendships")
        try {
            // Query for a friendship where the current user is userId1 and the friend is userId2
            val query1 = friendshipsCollection
                .whereEqualTo("userId1", currentUserId)
                .whereEqualTo("userId2", friendId)
                .get()
                .await()

            if (!query1.isEmpty) {
                return true // Friendship found with current user as userId1
                Log.e("friendship found",  "Friendship found with current user as userId1")
            }

            // Query for a friendship where the current user is userId2 and the friend is userId1
            val query2 = friendshipsCollection
                .whereEqualTo("userId1", friendId)
                .whereEqualTo("userId2", currentUserId)
                .get()
                .await()

            if (!query2.isEmpty) {
                return true // Friendship found with current user as userId2
            }
        } catch (e: Exception) {
            // Handle any errors, e.g., logging or rethrowing
            println("Error checking friendship: ${e.message}")
        }

        return false // No friendship found
    }


    // Function to fetch a friend's diary entries if a friendship exists
    suspend fun getFriendsDiaryEntries(userId: String, friendId: String, onResult: (List<DiaryEntry>) -> Unit) {
        val friendshipExists = checkFriendship(userId, friendId)

        if (friendshipExists) {
            firestoreDB.collection("diary_entries")
                .whereEqualTo("userId", friendId) // Assuming entries are filtered by the friend's userId
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get() // Note: Consider using addSnapshotListener for real-time updates if needed
                .addOnSuccessListener { snapshot ->
                    val diaryEntryList = snapshot.documents.mapNotNull {
                        it.toObject(DiaryEntry::class.java)
                    }
                    onResult(diaryEntryList)
                }
                .addOnFailureListener { e ->
                    // Log the error or handle it as needed
                    onResult(emptyList())
                }
        } else {
            // Handle the case where no friendship exists, e.g., return an empty list
            onResult(emptyList())
        }
    }

    fun addDiaryEntry(diaryEntry: DiaryEntry, onResult: (Boolean) -> Unit) {
        firestoreDB.collection("diary_entries").add(diaryEntry)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }



    // Add other methods as needed for updating, deleting, etc.
}