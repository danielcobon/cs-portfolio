package com.teamacupcake.secretdiaryapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.teamacupcake.secretdiaryapp.data.Friendship
import com.teamacupcake.secretdiaryapp.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FriendshipRepository(private val db: FirebaseFirestore) {

    private val _friendshipsUpdate = MutableLiveData<Unit>()
    val friendshipsUpdate: LiveData<Unit> = _friendshipsUpdate

    // Example method to check if a friendship exists
//    suspend fun checkFriendship(userId: String, friendId: String): Boolean {
//        // Implement the logic to check if a friendship exists in your database
//        // This could involve querying a 'friendships' collection for a document
//        // that represents a friendship between `userId` and `friendId`
//        return true // Placeholder return value
//    }

    suspend fun addFriendship(userId1: String, userId2: String): Boolean =
        suspendCoroutine { continuation ->
            val friendship = Friendship(userId1, userId2)
            db.collection("friendships").document("$userId1-$userId2").set(friendship)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
            _friendshipsUpdate.postValue(Unit)

        }
    suspend fun getFriendsList(userId: String): List<Friendship> = withContext(Dispatchers.IO) {
        val friendships = mutableListOf<Friendship>()
        try {
            // Query for friendships where the current user is either userId1 or userId2
            val querySnapshot = db.collection("friendships")
                .whereEqualTo("userId1", userId)
                .get()
                .await()
            querySnapshot.documents.forEach { document ->
                document.toObject(Friendship::class.java)?.let { friendship ->
                    friendships.add(friendship)
                }
            }

            // Repeat the query for userId2 if necessary, depending on your data structure and requirements
            // This second query might be needed if friendships can be initiated by either user

        } catch (e: Exception) {
            Log.e("FriendshipRepository", "Error fetching friends list", e)
        }
        return@withContext friendships
    }



    suspend fun fetchFriendDetailsById(friendId: String): User? = withContext(Dispatchers.IO) {
        try {
            val documentSnapshot = db.collection("users").document(friendId).get().await()
            return@withContext documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("FriendshipRepository", "Error fetching friend details", e)
            return@withContext null
        }
    }

//    suspend fun removeFriendship(friendshipId: Friendship) = withContext(Dispatchers.IO) {
//        try {
//            Log.e("FriendshipRepository", "$friendshipId")
//
////            db.collection("friendships").document(friendshipId.toString()).delete().await()
//        } catch (e: Exception) {
//            Log.e("FriendshipRepository", "Error removing friendship", e)
//        }
//    }
//suspend fun removeFriendship(friendId: userId2) = withContext(Dispatchers.IO) {
//        try {
//            Log.e("FriendshipRepository", "$userId2")
//
//            db.collection("friendships").document(friendId).delete().await()
//        } catch (e: Exception) {
//            Log.e("FriendshipRepository", "Error removing friendship", e)
//        }
//    }

}
