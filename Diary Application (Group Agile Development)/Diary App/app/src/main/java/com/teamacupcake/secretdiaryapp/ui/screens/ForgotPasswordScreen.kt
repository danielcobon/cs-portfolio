package com.teamacupcake.secretdiaryapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.teamacupcake.secretdiaryapp.MainActivity
import com.teamacupcake.secretdiaryapp.R


    @Composable
    fun ForgotPasswordPage(modifier: Modifier = Modifier, onNavigateToLogin: () -> Unit) {
        var email by remember { mutableStateOf("") }
        val auth = FirebaseAuth.getInstance()
        val context = LocalContext.current // Get the current context

        val snackbarHostState = remember { SnackbarHostState() }
        var snackbarMessage by remember { mutableStateOf("") }

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            //color can be changed in the res/values/colors.xml(temporary background currently in use)
            color = colorResource(id = R.color.temp_background)
        ){
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Reset your password",
                    style = MaterialTheme.typography.headlineSmall
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },

                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)

                )
                Button(
                    onClick = {
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Reset link sent. Check your email.", Toast.LENGTH_LONG).show()
                                } else {
                                    // Determine the specific error message
                                    val errorMsg = when (task.exception) {
                                        is FirebaseAuthInvalidUserException -> "Error: This email is not registered."
                                        is FirebaseAuthInvalidCredentialsException -> "Error: The email entered is invalid."
                                        else -> "Error: Failed to send reset link. Try again."
                                    }
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                ) {
                    Text("Send Reset Link")
                }
                Spacer(modifier = Modifier.weight(1f)) // Use for layout spacing or to push elements in a flexible box

                // Added TextButton for returning to login
                TextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        onNavigateToLogin()
                        println("back pressed")
                    }
                ) {
                    Text(
                        color = Color.White,
                        text = "Return to login?"
                    )
                }
            }
            // Show the Snackbar when snackbarMessage changes
            LaunchedEffect(snackbarMessage) {
                if (snackbarMessage.isNotEmpty()) {
                    snackbarHostState.showSnackbar(snackbarMessage)
                    snackbarMessage = "" // Reset the message to avoid repeated Snackbars
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ForgotPasswordPagePreview() {
        // Assuming you have a NavController available or just want to see the layout
        // In a real app, you'd pass actual NavController and other dependencies
        val navController = rememberNavController()
        ForgotPasswordPage(Modifier, onNavigateToLogin = {
            navController.navigate(MainActivity.NavItem.LogIn.route)
        })
    }


