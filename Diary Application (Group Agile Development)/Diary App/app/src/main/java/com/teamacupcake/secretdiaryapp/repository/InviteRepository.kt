package com.teamacupcake.secretdiaryapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.teamacupcake.secretdiaryapp.data.InviteCode

class InviteCodeRepository(private val db: FirebaseFirestore) {

    fun createInviteCode(inviteCode: InviteCode, onComplete: (Boolean) -> Unit) {
        db.collection("inviteCodes").document(inviteCode.code).set(inviteCode)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }


    fun getInviteCode(code: String, onComplete: (InviteCode?) -> Unit) {
        db.collection("inviteCodes").document(code)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val inviteCode = documentSnapshot.toObject(InviteCode::class.java)
                onComplete(inviteCode)
            }
            .addOnFailureListener { onComplete(null) }
    }

    fun markInviteCodeAsAccepted(code: String) {
        db.collection("inviteCodes").document(code)
            .update("accepted", true)
            .addOnSuccessListener {
                // Handle success, maybe log something or inform user
            }
            .addOnFailureListener {
                // Handle failure, log error or inform user
            }
    }
}