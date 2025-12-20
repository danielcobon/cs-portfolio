package com.teamacupcake.secretdiaryapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.teamacupcake.secretdiaryapp.R
import com.teamacupcake.secretdiaryapp.data.ChangeEmailViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmailAddressPage(
    onBackButtonPress: () -> Unit,
    onSignOut: () -> Unit,
    showSnackbarMessage: (msg: String) -> Unit,
    auth: FirebaseAuth
) {
    val viewModel = ChangeEmailViewModel(auth)
    var emailField by rememberSaveable { mutableStateOf("") }
    val buttonEnabled = emailField.isNotBlank()
    var verificationEmailSent by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    var snackbarMessage by remember { mutableStateOf("") }
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    val scope = rememberCoroutineScope()
    var emailFieldValid by rememberSaveable { mutableStateOf(true) }
//    val emailFieldColors = if (!emailFieldValid) {
//        colors(
//            focusedBorderColor = Color.Red ,
//            unfocusedBorderColor = Color.Red,
//        )
//    } else {
//        colors()
//    }
    var logOutPopUpVisible = remember { mutableStateOf(false) }

    @Composable
    fun LogOutPopUpWarning(onDismiss: () -> Unit) {
        AlertDialog(
            icon = {
                Icon(Icons.Filled.Info, contentDescription = "Example Icon")
            },
            title = {
                Text(text = "Warning")
            },
            text = {
                Text(
                    text = "After the verification email has been sent, you will be logged out. \n" +
                            "If you have clicked the link in the verification email you will be able to " +
                            "log back in with your updated e-mail address. Otherwise log back in with your " +
                            "old e-mail address."
                )
            },
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {

            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Ok")
                }
            }
        )
    }
    
    @Composable
    fun ReauthenticateUserPopUp(
        showSnackbar: () -> Unit
    ) {
        var email = rememberSaveable { auth.currentUser?.email.toString() }
        var password by rememberSaveable { mutableStateOf("") }
        var buttonClickable by rememberSaveable { mutableStateOf(false) }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        val kc = LocalSoftwareKeyboardController.current
        var passwordValid by rememberSaveable { mutableStateOf(true) }

        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "User Verification",
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            HorizontalDivider(
                modifier = Modifier.height(40.dp),
                color = Color.LightGray
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Please enter your password for re-authentication",
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
            )
            HorizontalDivider(
                modifier = Modifier.height(20.dp),
                color = Color.Transparent
            )
            //Email text field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = false,
                value = email,
                onValueChange = {
                    email = it.trim()
                    buttonClickable = it.isNotBlank() && password.isNotBlank()
                },
                label = { Text(text = stringResource(id = R.string.email_prompt)) },
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            //Password text field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = password,
                onValueChange = {
                    password = it
                    buttonClickable = it.isNotBlank()
                },
                label = { Text(text = stringResource(id = R.string.password_prompt)) },
                shape = RoundedCornerShape(8.dp),
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
                ),
                isError = !passwordValid,
                supportingText = {
                    if (!passwordValid) {
                        Text(text = "Invalid Password")
                    }
                }
            )
            // Cancel button
            Row(
                modifier = Modifier
            ) {
                FilledTonalButton(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f),
                    onClick = { viewModel.setDialogState(false) }
                ) {
                    Text(text = "Cancel")
                }
                //Re-authenticate button
                FilledTonalButton(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f),
                    onClick = {
                        //Re-authenticate user
                        val success = viewModel.reauthenticateUser(email, password) {successful ->
                            if (!successful) {
                                kc?.hide()
                                passwordValid = false
                            } else {
                                viewModel.setDialogState(false)
                                passwordValid = true
                                viewModel.setUserAuthenticated(true)
                                viewModel.updateExistingEmail(emailField) { verificationSent ->
                                    if (verificationSent) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Verification E-mail has been sent to $emailField",
                                                duration = SnackbarDuration.Short
                                            )
                                            showSnackbarMessage(
                                                "You have been logged out. \n" +
                                                        "Either login using your updated e-mail address after verifying " +
                                                        "or use your current e-mail address."
                                            )
                                            onSignOut()
                                        }
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("An error occurred while sending verification E-mail!")
                                        }
                                    }
                                }
                            }
                        }
                    },
                    enabled = buttonClickable
                ) {
                    Text(text = "Authenticate")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = "Change E-mail Address")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackButtonPress() }
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
        if (viewModel.displayDialog.collectAsState().value) {
            BasicAlertDialog(
                onDismissRequest = { viewModel.setDialogState(false) },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                )
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                ) {
                    ReauthenticateUserPopUp(
                        showSnackbar = {
                            viewModel.viewModelScope.launch {
                                snackbarHostState.showSnackbar(viewModel.snackbarMessage.value)
                            }
                        }
                    )
                }
            }
        }
        if (logOutPopUpVisible.value) {
            LogOutPopUpWarning(
                onDismiss = {
                    logOutPopUpVisible.value = false
                    viewModel.setDialogState(true)
                }
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it))
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
            ) {
                //Account details section
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Justify,
                        text = "Update your existing E-mail address by entering a new email address" +
                                " in the field below and pressing the Update button. A verification link " +
                                "will be sent to the new E-mail address.",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light
                        )
                    )
                    HorizontalDivider()
                    Text(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Justify,
                        text = "Current E-mail Address",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        modifier = Modifier
                            .padding(PaddingValues(horizontal = 10.dp, vertical = 10.dp))
                            .fillMaxWidth(),
                        textAlign = TextAlign.Justify,
                        text = viewModel.currentEmail.collectAsState().value.toString(),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                    )
                    HorizontalDivider()
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        value = emailField,
                        onValueChange = {email: String ->
                            emailField = email.trim()
                            if (emailField.isNotEmpty()) {
                                emailFieldValid = emailRegex.matches(emailField)
                            } else {
                                emailFieldValid = true
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.change_email_prompt)) },
                        shape = RoundedCornerShape(4.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        isError = !emailFieldValid,
                        supportingText = {
                            if (!emailFieldValid) { Text(text = "E-mail is formatted incorrectly!") }
                        }

                    )
//                    if (verificationEmailSent) {
//                        Text(
//                            modifier = Modifier
//                                .padding(PaddingValues(horizontal = 10.dp, vertical = 10.dp))
//                                .fillMaxWidth(),
//                            textAlign = TextAlign.Justify,
//                            text = "Verification Email has been sent to $emailField",
//                            style = TextStyle(
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Light,
//                                color = Color.Blue
//                            )
//                        )
//                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        onClick = {
                           if (!emailRegex.matches(emailField)) {
                               emailFieldValid = false
                               scope.launch() {
                                   snackbarHostState.showSnackbar("E-mail is formatted incorrectly!")
                               }
                           } else {
                               // Set dialog pop up to verify user
                               emailFieldValid = true
                               logOutPopUpVisible.value = true
                           }
                        },
                        enabled = buttonEnabled
                    ) {
                        Text(text = "Update")
                    }
                }
                HorizontalDivider()
            }
        }
    }

}

