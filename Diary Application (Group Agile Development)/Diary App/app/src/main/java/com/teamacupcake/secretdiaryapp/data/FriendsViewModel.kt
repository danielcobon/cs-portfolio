package com.teamacupcake.secretdiaryapp.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamacupcake.secretdiaryapp.repository.DiaryRepository
import com.teamacupcake.secretdiaryapp.repository.FriendshipRepository
import com.teamacupcake.secretdiaryapp.repository.InviteCodeRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resumeWithException

class FriendsViewModel(
    private val diaryEntryViewModel: DiaryEntryViewModel
) : ViewModel() {

    val auth = FirebaseAuth.getInstance()

    // Initialize FirebaseFirestore instance lazily
    private lateinit var db: FirebaseFirestore

    // lateinit var db: FirebaseFirestore

    //private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }


    private val _inviteStatus = MutableLiveData<Result<String>>()
    val inviteStatus: LiveData<Result<String>> = _inviteStatus

    // Example repository for fetching diary entries - you need to implement this
    private val diaryRepository = DiaryRepository()

    private val inviteCodeRepository = InviteCodeRepository(FirebaseFirestore.getInstance())

    private val friendshipRepository = FriendshipRepository(FirebaseFirestore.getInstance())


    private val _friendEntries = MutableLiveData<List<DiaryEntry>>()
    val friendEntries: LiveData<List<DiaryEntry>> = _friendEntries

    // LiveData to hold timeline entries
    private val _timelineEntries = MutableLiveData<List<DiaryEntry>>()
    val timelineEntries: LiveData<List<DiaryEntry>> = _timelineEntries

    private val _inviteCode = MutableLiveData<String>()
    val inviteCode: LiveData<String> = _inviteCode

    // Example of LiveData
    private val _friendsList = MutableLiveData<List<Friendship>>()
    val friendsList: LiveData<List<Friendship>> = _friendsList


    // Example of LiveData
    // val userEntries = MutableLiveData<List<DiaryEntry>>()
    //val combinedEntries: LiveData<List<DiaryEntry>> = combinedEntries

// Example of MutableStateFlow
// private val _friendsList = MutableStateFlow<List<String>>(emptyList())
// val friendsList: StateFlow<List<String>> = _friendsList


    init {
        db = FirebaseFirestore.getInstance()
        // Observe friendship updates
        friendshipRepository.friendshipsUpdate.observeForever {
            fetchTimelineForFriends()
        }
    }

    val combinedEntries: LiveData<List<DiaryEntry>> = MediatorLiveData<List<DiaryEntry>>().apply {
        addSource(friendEntries) { friendEntries ->
            value = combineEntries(friendEntries, diaryEntryViewModel.entries.value.orEmpty())
        }
        addSource(diaryEntryViewModel.entries) { userEntries ->
            value = combineEntries(friendEntries.value.orEmpty(), userEntries.orEmpty())
        }
    }

    private fun combineEntries(friendEntries: List<DiaryEntry>, userEntries: List<DiaryEntry>): List<DiaryEntry> {
        return friendEntries + userEntries
    }


    fun fetchFriendProfile(userId: String, listener: ProfileFetchListener) {
        val db = FirebaseFirestore.getInstance()
        db.collection("userProfiles").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val username = documentSnapshot.getString("username") ?: "Unknown User"
                val profilePicUrl = documentSnapshot.getString("profilePicUrl") ?: ""
                listener.onProfileFetched(UserProfile(username, profilePicUrl))
            }.addOnFailureListener { exception ->
                listener.onError(exception)
            }
    }



    private fun fetchTimelineForFriends() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: return
        viewModelScope.launch {
            val friendIds = friendshipRepository.getFriendsList(userId)
            // Fetch friend details; ensure this now correctly returns List<FriendDetail?>
//            val friendsDetails = friendIds.mapNotNull { id ->
//                f//riendshipRepository.fetchFriendDetailsById(id) // Ensure this returns FriendDetail?
//            }

        }
    }


    fun fetchFriends() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val friendsList = friendshipRepository.getFriendsList(userId)
                if (friendsList.isEmpty()) {
                    Log.d("FriendsViewModel", "Friends list is empty")
                } else {
                    _friendsList.value = friendsList
                    Log.d("FriendsViewModel", "Fetched friends list: ${friendsList.joinToString()}")
                }
            } catch (e: Exception) {
                Log.e("FriendsViewModel", "Failed to fetch friends", e)
            }
        }
    }

//    fun removeFriendshipByUserId(userId: String) {
//        viewModelScope.launch {
//            findFriendshipByUserId(userId,
//                onSuccess = { friendship ->
//                    removeFriendship(friendship)
//                    fetchFriends() // Refresh the friends list
//                },
//                onFailure = { exception ->
//                    Log.e("FriendViewModel", "Failed to find friendship", exception)
//                }
//
//            )
//        }
//    }
fun removeFriendshipByUserId(currentUserId: String, friendId: String) {
    viewModelScope.launch {
        try {
            // Perform the deletion and refresh the friends list
            deleteFriendship(currentUserId, friendId)
            fetchFriends()
        } catch (e: Exception) {
            Log.e("FriendViewModel", "Failed to remove friendship", e)
        }
    }
}

    suspend fun deleteFriendship(currentUserId: String, friendId: String) = withContext(Dispatchers.IO) {
        val friendshipQuery1 = db.collection("friendships")
            .whereEqualTo("userId1", currentUserId)
            .whereEqualTo("userId2", friendId)
            .get()
            .await()

        for (document in friendshipQuery1.documents) {
            db.collection("friendships").document(document.id).delete().await()
        }

        val friendshipQuery2 = db.collection("friendships")
            .whereEqualTo("userId1", friendId)
            .whereEqualTo("userId2", currentUserId)
            .get()
            .await()

        for (document in friendshipQuery2.documents) {
            db.collection("friendships").document(document.id).delete().await()
        }
    }


    fun findFriendshipByUserId(userId: String, onSuccess: (Friendship) -> Unit, onFailure: (Exception) -> Unit) {
        FirebaseFirestore.getInstance().collection("friendships")
            .whereEqualTo("userId2", userId) // Assuming 'userId' is the field name in your documents
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Assuming only one friendship is returned per user
                    val friendship = querySnapshot.documents.first().toObject(Friendship::class.java)
                    friendship?.let { onSuccess(it) }
                } else {
                    onFailure(Exception("No friendship found for user ID: $userId"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


//    fun findFriendshipByUserId(userId: String, onSuccess: (Friendship) -> Unit, onFailure: (Exception) -> Unit) {
//        FirebaseFirestore.getInstance().collection("friendships")
//            .whereEqualTo("userId", userId) // Assuming 'userId' is the field name in your documents
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if (!querySnapshot.isEmpty) {
//                    // Assuming only one friendship is returned per user
//                    val friendship = querySnapshot.documents.first().toObject(Friendship::class.java)
//                    friendship?.let { onSuccess(it) }
//                } else {
//                    onFailure(Exception("No friendship found for user ID: $userId"))
//                }
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }

//    fun removeFriendship(friendshipId: Friendship) {
//        viewModelScope.launch {
//            friendshipRepository.removeFriendship(friendshipId)
//            //fetchFriends() // Refresh the list after removal
//        }
//    }

    fun createAndShareInviteCode(userId: String) {

        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: return
        val newInviteCode = UUID.randomUUID().toString().take(8) // Generate the code
        val inviteCode = InviteCode(
            code = newInviteCode,
            userId = userId,
            accepted = false,
            timestamp = System.currentTimeMillis()
        )
        _inviteCode.postValue(newInviteCode)

        inviteCodeRepository.createInviteCode(inviteCode) { success ->
            if (success) {
                // Proceed with sharing the invite code
                _inviteStatus.postValue(Result.success(newInviteCode))
            } else {
                // Handle the error
                _inviteStatus.postValue(Result.failure(Exception("Failed to create invite code.")))
            }
        }
    }

    fun getFriendEntries(currentUserId: String?, friendIds: List<String>) {


        viewModelScope.launch(Dispatchers.IO) {
            // Launch async operations for each friendId to fetch their entries concurrently
            val entriesDeferred = friendIds.map { friendId ->
                async {
                    fetchEntriesByFriendId(friendId)
                }
            }

            // Await all async operations to complete and flatten the result into a single list
            val allEntries = entriesDeferred.awaitAll().flatten()

            // Post the result to LiveData on the main thread
            withContext(Dispatchers.Main) {
                _friendEntries.value = allEntries
            }
        }
    }

    // Simulated function to fetch diary entries by friendId
    // Replace with your actual logic to fetch from Firestore or your backend
     suspend fun fetchEntriesByFriendId(friendId: String): List<DiaryEntry> {
        // Simulate network request delay
        delay(1000)

        val entries = mutableListOf<DiaryEntry>()
        try {
            // Attempt to fetch entries from Firestore for the given friendId
            val documents = db.collection("diaryEntries")
                .whereEqualTo("userId", friendId)
                .get()
                .await() // Use await to ensure the async Firestore call completes

            for (document in documents) {
                document.toObject(DiaryEntry::class.java)?.let {
                    entries.add(it)
                }
            }

            // Log the fetched entries along with the friendId
            Log.d(
                "FriendEntries",
                "Entries for friendId $friendId: ${entries.joinToString { it.toString() }}"
            )
        } catch (e: Exception) {
            // Log an error if fetching entries fails
            Log.e("FriendEntries", "Error fetching entries for friendId $friendId", e)
        }

        return entries
    }



    fun submitInviteCode(context: Context, code: String, currentUserCode: String?) {
        viewModelScope.launch {
            try {
                if (currentUserCode != null) {
                    verifyInviteCode(code, currentUserCode)
                }
            } catch (e: Exception) {
                _inviteStatus.postValue(Result.failure(e))
            }
        }
    }


    private suspend fun verifyInviteCode(code: String, currentUserCode: String) =
        withContext(Dispatchers.IO) {
            if (code.isBlank()) {
                throw IllegalArgumentException("The invite code cannot be empty")
            }

            if (code == currentUserCode) {
                throw IllegalArgumentException("You cannot use your own invite code")
            }

            val inviteCode = async {
                val invite = CompletableDeferred<InviteCode?>()
                inviteCodeRepository.getInviteCode(code) { invite.complete(it) }
                invite.await()
            }.await()

            inviteCode?.let {
                if (!isInviteCodeNotExpired(it) || it.accepted) {
                    throw IllegalArgumentException("Invite code is invalid or expired.")
                } else {
                    // Mark as accepted and update Firestore
                    inviteCodeRepository.markInviteCodeAsAccepted(code)

                    // Assuming addFriendship is now a suspend function
                    val currentUser =
                        auth.currentUser?.uid ?: throw Exception("Current user not found.")
                    val inviteCreatorId =
                        it.userId // Assume you can get this from the invite code object

                    try {
                        val success =
                            friendshipRepository.addFriendship(currentUser, inviteCreatorId)
                        if (success) {
                            withContext(Dispatchers.Main) {
                                _inviteStatus.postValue(Result.success("Friendship created successfully."))
                            }
                        } else {
                            throw Exception("Failed to create friendship.")
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            _inviteStatus.postValue(Result.failure(Exception("Failed to create friendship.")))
                        }
                    }
                }
            } ?: throw IllegalArgumentException("Invite code does not exist.")
        }

    fun isInviteCodeNotExpired(inviteCode: InviteCode): Boolean {
        val currentTime = System.currentTimeMillis()
        val timePassed = currentTime - inviteCode.timestamp
        val sixtyMinutesInMillis = 60 * 60 * 1000
        return timePassed <= sixtyMinutesInMillis
    }

    interface ProfileFetchListener {
        fun onProfileFetched(profile: UserProfile)
        fun onError(error: Exception)
    }


    fun fetchFriendProfileForTimeline(userId: String, onProfileFetched: (String, String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("userProfiles").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                // Assuming 'username' and 'profilePicUrl' are the fields in your Firestore document
                val username = documentSnapshot.getString("username") ?: "Unknown User"
                val profilePicUrl = documentSnapshot.getString("profilePicUrl")
                    ?: "" // Provide a default or empty string if not found
                onProfileFetched(username, profilePicUrl)
            }.addOnFailureListener {
                Log.e("FetchFriendProfile", "Error fetching profile", it)
                onProfileFetched("Unknown User", "") // Handle failure case
            }
    }


}
// Extension function to adapt fetchFriendProfile to coroutines
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun FriendsViewModel.fetchFriendProfileCoroutine(userId: String): UserProfile =
    suspendCancellableCoroutine { continuation ->
        this.fetchFriendProfile(userId, object : FriendsViewModel.ProfileFetchListener {
            override fun onProfileFetched(profile: UserProfile) {
                continuation.resume(profile) {}
            }

            override fun onError(error: Exception) {
                continuation.resumeWithException(error)
            }
        })
    }


class FriendsViewModelFactory(private val diaryEntryViewModel: DiaryEntryViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendsViewModel(diaryEntryViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

