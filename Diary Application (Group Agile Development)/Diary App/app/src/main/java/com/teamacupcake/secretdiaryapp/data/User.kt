package com.teamacupcake.secretdiaryapp.data

data class User(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val profileImageUrl: String? = null,
    val bio: String = "",
) {
    // Empty constructor for Firestore serialization
    constructor() : this("", "", "", null, "")
}
