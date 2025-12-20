package com.teamacupcake.secretdiaryapp.ui.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.teamacupcake.secretdiaryapp.MainActivity
import com.teamacupcake.secretdiaryapp.ui.components.SettingsItem


@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingsPage(
        navController: NavController, // Pass NavController here
        onNavigateToAccount: () -> Unit
    ) {
        //Run the code
        SettingsScreen(navController)
    }
// Page to allow users to change settings regarding their account.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(    navController: NavController // Pass NavController here
) {
    //List of settings
    val settingItems = listOf (
        SettingsItem.Account
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = MainActivity.NavItem.Settings.title)
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
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 20.dp)
        ) {
            items(settingItems) {item ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate(item.navItem.route) }
                    ) {
                        Icon(item.icon, contentDescription = item.iconDesc)
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
