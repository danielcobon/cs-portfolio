package com.teamacupcake.secretdiaryapp.ui.screens

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.teamacupcake.secretdiaryapp.R
import com.teamacupcake.secretdiaryapp.data.SignUpViewModel


@Composable
fun SignupPage(signUpViewModel: SignUpViewModel, onNavigateToLogin: () -> Unit) {
    val context = LocalContext.current
    val signUpResult by signUpViewModel.signUpResult.observeAsState()

    LaunchedEffect(signUpResult) {
        signUpResult?.onSuccess { userEmail ->
            Toast.makeText(context, "Account created for: $userEmail", Toast.LENGTH_LONG).show()
            //onNavigateToLogin()
        }?.onFailure { exception ->
            Toast.makeText(context, exception.localizedMessage ?: "Sign up failed", Toast.LENGTH_LONG).show()
        }
    }



        @Composable
        fun SignupScreen(modifier: Modifier = Modifier) {

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
                    //Text button to go to Login Screen
                    TextButton(
                        modifier = Modifier.align(Alignment.Start),
                        contentPadding = PaddingValues(0.dp),
                        onClick = {
                            onNavigateToLogin()
                        }
                    ) {
                        Text(
                            color = Color.White,
                            text = "Already have an account?"
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        onClick = { signUpViewModel.createAccount(email, password) },
                        enabled = buttonClickable
                    ) {
                        Text(text = stringResource(id = R.string.sign_up_button))
                    }
                }
            }
        }
    SignupScreen()
        }



