package com.teamacupcake.secretdiaryapp.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.teamacupcake.secretdiaryapp.R

//class LoginScreen {
//companion object {
    @Composable
    fun LoginPage(
        auth: FirebaseAuth,
        context: Context,
        onNavigateToHome: () -> Unit,
        onNavigateToSignup: () -> Unit,
        onNavigateToForgotPassword: () -> Unit
    ) {

        fun loginWithEmailAndPassword(email: String, password: String) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() {
                    if (it.isSuccessful) {
                        val user = it.result?.user
                        Toast.makeText(
                            context,
                            "Login with $email Successful.",
                            Toast.LENGTH_LONG
                        ).show()
                        onNavigateToHome()
                    } else {
                        val exceptionToastMsg = when (it.exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Error: Email/Password is incorrect. Please try again."
                            is FirebaseAuthInvalidUserException -> "Error: An account with the given Email does not exist."
                            is FirebaseTooManyRequestsException -> "Error: Too many incorrect login attempts were made"
                            is FirebaseAuthEmailException -> "${it.exception?.message}"
                            else -> "${it.exception?.message}"
                        }
                        Toast.makeText(
                            context,
                            exceptionToastMsg,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        @Composable
        fun LoginScreen() {
            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            var buttonClickable by rememberSaveable { mutableStateOf(false) }
            var passwordVisible by rememberSaveable { mutableStateOf(false) }

            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                //color can be changed in the res/values/colors.xml(temporary background currently in use)
                color = colorResource(id = R.color.temp_background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    //Email text field
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = email,
                        onValueChange = {
                            email = it
                            buttonClickable = it.isNotBlank() && password.isNotBlank()
                        },
                        label = { Text(text = stringResource(id = R.string.email_prompt)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            disabledTextColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    //Password text field
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = password,
                        onValueChange = {
                            password = it
                            buttonClickable = it.isNotBlank() && email.isNotBlank()
                        },
                        label = { Text(text = stringResource(id = R.string.password_prompt)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            disabledTextColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            //Lock icon button to toggle password visibility
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (passwordVisible) Color.Gray else Color.Black
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )
                    //Text button to go to Sign up page
                    TextButton(
                        modifier = Modifier.align(Alignment.Start),
                        contentPadding = PaddingValues(0.dp),
                        onClick = { onNavigateToSignup() }
                    ) {
                        Text(
                            color = Color.White,
                            text = "Sign Up"
                        )
                    }
                    //Text button to go to Login Screen
                    TextButton(
                        modifier = Modifier.align(Alignment.Start),
                        contentPadding = PaddingValues(0.dp),
                        onClick = {
                            onNavigateToForgotPassword()
                        }
                    ) {
                        Text(
                            color = Color.White,
                            text = "Forgot Password?"
                        )
                    }
                    //Login button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        onClick = {
                            loginWithEmailAndPassword(email, password)
                        },
                        enabled = buttonClickable
                    ) {
                        Text(text = stringResource(id = R.string.login_button))
                    }
                }
            }
        }
        LoginScreen()
    }


