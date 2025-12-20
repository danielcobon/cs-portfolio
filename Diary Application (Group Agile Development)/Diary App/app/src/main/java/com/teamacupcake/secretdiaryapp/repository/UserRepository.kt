package com.teamacupcake.secretdiaryapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.teamacupcake.secretdiaryapp.data.User

class UserRepository(private val db: FirebaseFirestore) {

    fun getUser(userId: String, onComplete: (User?) -> Unit) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                onComplete(user)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }


//    fun updateUsername(userId: String, newUsername: String) {
//        val userRef = db.collection("userProfile").document(userId)
//        userRef.update("username", newUsername).addOnSuccessListener {
//            // Log success or inform the user
//        }.addOnFailureListener { e ->
//            // Handle error
//        }
//    }


    // Add more user-related operations here...
}
