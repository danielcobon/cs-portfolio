package com.teamacupcake.secretdiaryapp.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.teamacupcake.secretdiaryapp.MainActivity
import com.teamacupcake.secretdiaryapp.ui.components.AccountItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AccountSettingsPage(
        onNavigateToChangeEmail: () -> Unit,
        onLogout: () -> Unit,
        context: Context,
        auth: FirebaseAuth,
        navController: NavController // Pass NavController here

    ) {
        fun logout() {
            auth.signOut()
            onLogout()
            Toast.makeText(
                context,
                "Successfully Logged out.",
                Toast.LENGTH_SHORT
            ).show()
        }


        // Page to allow users to change settings regarding their account.
        @Composable
        fun AccountSettingsScreen() {
            val settingItems = listOf (
                AccountItem.ChangeEmailAddress
            )
            val user = auth.currentUser
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = MainActivity.NavItem.AccountSettings.title)
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { navController.popBackStack() }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back button icon"
                                )
                            }
                        }
                    )
                }
            ) {
                //Settings page code
                Box(modifier = Modifier.padding(it)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 20.dp)
                    ) {
                        item {
                            //Account details section
                            HorizontalDivider()
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    modifier = Modifier.padding(6.dp),
                                    textAlign = TextAlign.Start,
                                    text = "Account Details",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                user?.let {
                                    Row(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(text = "Email Address: ")
                                        Text(text = it.email.toString())
                                    }
                                }
                                TextButton(
                                    modifier = Modifier.padding(6.dp),
                                    onClick = {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            logout()
                                            this.cancel()
                                        }
                                    }
                                ) {
                                    Text(text = "Logout")
                                }
                            }
                            HorizontalDivider()
                        }
                        items(settingItems) {item ->
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { navController.navigate(item.navItem.route) }
                                ) {

                                    Icon(Icons.Outlined.Email, contentDescription = item.iconDesc)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            modifier = Modifier,
                                            text = item.title,
                                            style = TextStyle(
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            modifier = Modifier,
                                            text = item.settingDesc,
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Light)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Run the code
        AccountSettingsScreen()
    }


