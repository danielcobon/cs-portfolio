package com.teamacupcake.secretdiaryapp.data

data class Friendship(
    val userId1: String = "",
    val userId2: String = "",
    val name1: String = "",
    val name2: String = ""

) {
    // Empty constructor for Firestore serialization
    constructor() : this("", "")
}
