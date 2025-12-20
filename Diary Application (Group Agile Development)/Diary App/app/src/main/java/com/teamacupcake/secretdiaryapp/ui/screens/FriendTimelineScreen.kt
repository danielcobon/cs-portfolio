package com.teamacupcake.secretdiaryapp.ui.screens


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.teamacupcake.secretdiaryapp.data.DiaryEntry
import com.teamacupcake.secretdiaryapp.data.FriendsViewModel
import com.teamacupcake.secretdiaryapp.data.ProfileViewModel
import com.teamacupcake.secretdiaryapp.data.UserProfile
import com.teamacupcake.secretdiaryapp.ui.components.DiaryEntryItem
import com.teamacupcake.secretdiaryapp.ui.utils.getTimeAgo

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FriendTimelineScreen(viewModel: FriendsViewModel, navController: NavController, profileViewModel: ProfileViewModel
) {
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid.orEmpty()

    val friendsList by viewModel.friendsList.observeAsState(initial = emptyList())
//    val entries by viewModel.friendEntries.observeAsState(initial = emptyList()).collectAsState(initial = emptyList()).sortedByDescending { it.timestamp }

    // Collect and sort entries within the composable
    val entries by viewModel.friendEntries.observeAsState(initial = emptyList())
    val sortedEntries = entries.sortedByDescending { it.timestamp }

    // Call fetchFriends to load friends when the screen is composed
    LaunchedEffect(Unit) {
        viewModel.fetchFriends()
    }

    //Trigger fetching friend entries based on the current list of friends
    LaunchedEffect(friendsList) {
        val friendIds = friendsList.map { it.userId2 }
Log.e("Friends List: ", friendsList.toString())
        viewModel.getFriendEntries(currentUserId, friendIds)
        }


    Column(modifier = Modifier.padding(16.dp)) {
        Text("Friend's Diary Entries", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(sortedEntries) { entry ->
                FriendDiaryEntryItem(
                    userId = entry.userId!!, // Ensure entry includes userId
                    entry = entry,
                    viewModel = viewModel,
                    onPasswordEntered = { /* Implement as needed */ },
                    onEntryClick = { /* Implement as needed */ },
                    onEntryDeleted = { /* Implement if needed */ },
                    onNavigateToHome = { /* Implement as needed */ },
                    navController =  navController,
                    profileViewModel = profileViewModel
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FriendDiaryEntryItem(
    userId: String,
    entry: DiaryEntry,
    viewModel: FriendsViewModel,
    onPasswordEntered: (String) -> Unit,
    onEntryClick: (DiaryEntry) -> Unit,
    onEntryDeleted: (DiaryEntry) -> Unit,
    onNavigateToHome: () -> Unit,
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    var friendUsername by remember { mutableStateOf("") }
    var friendProfilePicUrl by remember { mutableStateOf("") }
    var userProfile by remember { mutableStateOf(UserProfile()) }


    LaunchedEffect(userId) {
        // Consider using a coroutine to fetch the user profile and update the state directly
        userProfile =
            entry.userId?.let { profileViewModel.fetchUserProfileSuspend(it) } ?: UserProfile(
                "Loading...",
                ""
            )
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Profile picture
            if (userProfile.profilePictureUrl.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(userProfile.profilePictureUrl),
                    contentDescription = "$friendUsername's profile picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Default profile picture",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Username and time ago
            Text(text = userProfile.username, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = getTimeAgo(entry.timestamp),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Entry content
        DiaryEntryItem(
            entry = entry,
            onPasswordEntered = onPasswordEntered,
            onEntryClick = onEntryClick,
            onEntryDeleted = onEntryDeleted,
            onNavigateToHome = onNavigateToHome,
            isFriendEntry = true, // This should hide the options menu for friend's entries,
            navController =  navController
        )
    }
}

