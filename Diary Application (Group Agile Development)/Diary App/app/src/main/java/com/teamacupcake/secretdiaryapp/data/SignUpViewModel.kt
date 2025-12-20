package com.teamacupcake.secretdiaryapp.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// SignUpViewModel.kt
class SignUpViewModel : ViewModel() {
    private val _signUpResult = MutableLiveData<Result<String>>()
    val signUpResult: LiveData<Result<String>> = _signUpResult

    fun createAccount(email: String, password: String) {
        val auth = FirebaseAuth.getInstance() // Access FirebaseAuth instance directly

        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val userEmail = result.user?.email ?: ""
                _signUpResult.postValue(Result.success(userEmail))
            } catch (e: Exception) {
                _signUpResult.postValue(Result.failure(e))
            }
        }
    }
}
//    fun createAccount(
//        email: String,
//        password: String,
//        auth: FirebaseAuth,
//        context: Context
//    ) {
//        viewModelScope.launch {
//
//            //auth code here
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener() { task ->
//                    if (task.isSuccessful) {
//                        // User creation was successful
//                        val user = task.result?.user
//                        val userEmail = user?.email
//                        val signUpSuccessfulToastMsg =
//                            "An account with the email: $userEmail successfully created. "
//                        Toast.makeText(
//                            context,
//                            signUpSuccessfulToastMsg,
//                            Toast.LENGTH_LONG
//                        ).show()
//                       // MainActivity.onNavigateToLogin()
//                    } else {
//                        // User creation failed
//                        val exceptionToastMsg = when (task.exception) {
//                            is FirebaseAuthUserCollisionException -> "Email: An account with this Email address already exists."
//                            is FirebaseAuthWeakPasswordException -> "Password: Please enter a minimum of 6 characters."
//                            else -> "${task.exception?.message}"
//                        }
//                        Toast.makeText(
//                            context,
//                            exceptionToastMsg,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//        }
//    }
//}
//
