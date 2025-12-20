package com.teamacupcake.secretdiaryapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import coil.compose.rememberImagePainter
import com.teamacupcake.secretdiaryapp.data.FriendsViewModel
import com.teamacupcake.secretdiaryapp.data.Friendship
import com.teamacupcake.secretdiaryapp.data.ProfileViewModel
import com.teamacupcake.secretdiaryapp.data.UserProfile


@Composable
fun FriendsScreen(viewModel: FriendsViewModel, profileViewModel: ProfileViewModel) {
    val friendsList by viewModel.friendsList.observeAsState(initial = emptyList())
   // var deleteClicked by remember { mutableStateOf(false) } // New state for delete confirmation
    LaunchedEffect(Unit) {
        viewModel.fetchFriends()
        //Log.d("Did removal work?: ", "$friendsList")

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("My Friends", style = MaterialTheme.typography.headlineMedium)
        Text("Number of friends: ${friendsList.size}", style = MaterialTheme.typography.bodyLarge)
        LazyColumn {
            //friendid.forEach { friends ->
            items(friendsList) { friendship ->
                // Assuming Friendship contains a userId field
                FriendProfileItem(userId = friendship.userId2, viewModel, profileViewModel)
                Log.d("Did removal work?: ", "$friendship")

            }
        }
    }
}

@Composable
fun FriendItem(userProfile: UserProfile, friendsViewModel: FriendsViewModel, profileViewModel: ProfileViewModel, onRemoveClicked: () -> Unit) {
    var deleteClicked by remember { mutableStateOf(false) } // New state for delete confirmation
//    val currentUserId: String?
//    get() = auth.currentUser?.uid

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (userProfile.profilePictureUrl.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(userProfile.profilePictureUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape) // Assuming you want a circular image
            )
        } else {
            // Display a placeholder if there's no URL
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Text(userProfile.username, modifier = Modifier.weight(1f))
        Button(onClick = { deleteClicked = true }) {
            Text("Remove")
        }
    }

    // Delete confirmation dialog
    if (deleteClicked) {
        AlertDialog(
            onDismissRequest = { deleteClicked = false },
            title = { Text("Confirm Friend Delete") },
            text = { Text("Are you sure you want to delete this Friend?") },
            confirmButton = {
                Button(onClick = {
                    profileViewModel.currentUserId?.let {
                        friendsViewModel.removeFriendshipByUserId(
                            it, userProfile.userId)
                    }
                    //friendsViewModel.fetchFriends()
//                    Log.d("After removal: ", "$friendsList")
                    deleteClicked = false // Close the dialog
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { deleteClicked = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

//
//        Button(onClick = {deleteClicked = true}) {
////deleteClicked = true
//            // Delete confirmation dialog
//            if (deleteClicked) {
//                AlertDialog(
//                    onDismissRequest = {
//                        deleteClicked = false
//                    },
//                    title = { Text("Confirm Friend Delete") },
//                    text = { Text("Are you sure you want to delete this Friend?") },
//                    confirmButton = {
//                        Button(onClick = {
//                            friendsViewModel.removeFriendshipByUserId(userProfile.userId)
//
//                            deleteClicked = false // Close the dialog
//
//                        }) {
//                            Text("Confirm")
//                        }
//                    },
//                    dismissButton = {
//                        Button(onClick = {
//                            deleteClicked = false
//
//                        }
//                        ) {
//                            Text("Cancel")
//                        }
//                    }
//                )
//            }
//
//            Text("Remove")
//        }
//    }
//}


        @Composable
        fun FriendProfileItem(
            userId: String,
            friendsViewModel: FriendsViewModel,
            profileViewModel: ProfileViewModel
        ) {
            // Instead of observing LiveData here, call a suspend function or another non-LiveData function
            // that can fetch the UserProfile without affecting the rest of the composable items
            var userProfile by remember { mutableStateOf(UserProfile()) }

            LaunchedEffect(userId) {
                // Consider using a coroutine to fetch the user profile and update the state directly
                userProfile = profileViewModel.fetchUserProfileSuspend(userId) ?: UserProfile(
                    "Loading...",
                    ""
                )
            }

            // Now pass the userProfile to FriendItem
            FriendItem(userProfile, friendsViewModel, profileViewModel) {
                profileViewModel.currentUserId?.let {
                    friendsViewModel.removeFriendshipByUserId(
                        it,
                        userId
                    )
                }
                friendsViewModel.fetchFriends()
            }
        }





