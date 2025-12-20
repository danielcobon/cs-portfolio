package com.teamacupcake.secretdiaryapp.data

data class InviteCode(
    val code: String = "",
    val userId: String = "", // The ID of the user who created the invite
    val accepted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis() // When the invite code was generated
) {
    // Empty constructor for Firestore serialization
    constructor() : this("", "", false, 0L)
}