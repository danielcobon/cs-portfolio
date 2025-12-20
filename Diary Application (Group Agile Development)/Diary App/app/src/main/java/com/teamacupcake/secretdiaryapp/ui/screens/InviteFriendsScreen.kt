package com.teamacupcake.secretdiaryapp.ui.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.teamacupcake.secretdiaryapp.data.FriendsViewModel
import java.util.*
@Composable
fun InviteFriendsScreen(friendsViewModel: FriendsViewModel) {
    val context = LocalContext.current
    val inviteStatus by friendsViewModel.inviteStatus.observeAsState()
    val inviteCode by friendsViewModel.inviteCode.observeAsState()
    val enteredCode = remember { mutableStateOf("") }

    // React to inviteStatus updates
    inviteStatus?.let { result ->
        // Since Toast can't be directly called inside Composable, use LaunchedEffect with unique keys
        LaunchedEffect(result) {
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                },
                onFailure = { throwable ->
                    Toast.makeText(context, throwable.message ?: "An error occurred", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Invite Friends", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                FirebaseAuth.getInstance().currentUser?.uid?.let {
                    friendsViewModel.createAndShareInviteCode(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Invite Code")
        }

        inviteCode?.let { code ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your Invite Code: $code")
            Button(
                onClick = { shareInviteCode(context, code) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Share Invite Code")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = enteredCode.value,
            onValueChange = {
                if (it.length <= 8) enteredCode.value = it
            },
            label = { Text("Enter Friend's Invite Code") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (enteredCode.value.isNotEmpty()) {
                    friendsViewModel.submitInviteCode(context, enteredCode.value, inviteCode ?: "")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enteredCode.value.isNotEmpty()
        ) {
            Text("Add Friend")
        }
    }
}


fun shareInviteCode(context: Context, inviteCode: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Want to look inside my Secret Diary? Here's my invite code: $inviteCode")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}




//
//    inviteStatus.observeAsState().value?.let { result ->
//        result.fold(onSuccess = {
//            Toast.makeText(context, "Success: $it", Toast.LENGTH_LONG).show()
//        }, onFailure = {
//            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
//        })
//    }