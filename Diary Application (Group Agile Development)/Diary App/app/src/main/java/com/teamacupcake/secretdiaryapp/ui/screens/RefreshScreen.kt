package com.teamacupcake.secretdiaryapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun RefreshScreen(navController: NavController,
){
    navController.navigate("Home") // Add the route name of your RefreshScreen here

}