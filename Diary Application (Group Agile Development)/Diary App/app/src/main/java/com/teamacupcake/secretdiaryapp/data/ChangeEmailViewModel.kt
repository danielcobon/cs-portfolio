package com.teamacupcake.secretdiaryapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangeEmailViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _currentEmail = MutableStateFlow(auth.currentUser?.email)
    val currentEmail: StateFlow<String?> = _currentEmail.asStateFlow()
    private val _verificationEmailSent = MutableStateFlow(false)
    val verificationEmailSent: StateFlow<Boolean> = _verificationEmailSent.asStateFlow()
    private val _emailVerified = MutableStateFlow(auth.currentUser?.isEmailVerified)
    val emailVerified: StateFlow<Boolean?> = _emailVerified.asStateFlow()
    private val _userAuthenticated = MutableStateFlow(false)
    val userAuthenticated: StateFlow<Boolean> = _userAuthenticated.asStateFlow()
    private val _displayDialog = MutableStateFlow(false)
    val displayDialog: StateFlow<Boolean> = _displayDialog.asStateFlow()
    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage.asStateFlow()
    private var _exceptionMessage = MutableStateFlow("")
    val exceptionMessage: StateFlow<String> = _exceptionMessage.asStateFlow()


//    public enum class AuthenticationFeedback {
//        INVALID_USER,
//        INVALID_CREDENTIALS
//    }

    public fun setDialogState(state: Boolean) {
        _displayDialog.value = state
    }

    public fun setSnackbarMessage(message: String) {
        _snackbarMessage.value = message
    }

    public fun setUserAuthenticated(state: Boolean) {
        _userAuthenticated.value = state
    }

    fun reauthenticateUser(
        email: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener { task ->
            val userReauthenticated = task.isSuccessful
            _exceptionMessage.value = task.exception.toString()
            callback(userReauthenticated)
        }
    }

    public fun updateExistingEmail(email: String, callback: (Boolean) -> Unit) {
        // Code to update email for firebase auth
        auth.currentUser?.let { currentUser ->
            viewModelScope.launch {
                currentUser.verifyBeforeUpdateEmail(email).addOnCompleteListener{task ->
                    callback(task.isSuccessful)
                }
            }
        }
    }
}