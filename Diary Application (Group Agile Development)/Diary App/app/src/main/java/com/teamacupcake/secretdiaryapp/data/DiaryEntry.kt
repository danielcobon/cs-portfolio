package com.teamacupcake.secretdiaryapp.data

data class DiaryEntry(
    var title: String = "",
    var content: String = "",
    var mood: String = "",
    val timestamp: Long = 0L,
    var imageUrl: String? = null, // Optional field for image URL
    val userId: String? = "", // User ID from Firebase Authentication
    var docId: String = "", // for keeping Firestore document ID
    val date: String = "", // Format: "yyyy-MM-dd", providing a default value
    val passLocked: Boolean = false, // Flag to indicate if the entry is secret
    val password: String = "", // Password for accessing the secret entry
    val deleted: Boolean = false,
    val private: Boolean = true,
    val hasImage: Boolean = true,
    val latitude: Double? = null,
    val locationName: String? = null,
    val longitude: Double? = null,
    val storageReference: String? = null
) {
    //var entryImageUrl: String

    // Empty constructor for Firestore serialization
    constructor() : this("", "", "", 0, null, "", "", "", false, "", false, true, false, null, null, null, null)
}
//enum class EntryType {
//    USER, FRIEND
//}

