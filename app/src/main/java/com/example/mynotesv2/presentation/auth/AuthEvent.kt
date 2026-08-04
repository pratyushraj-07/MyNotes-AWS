package com.example.mynotesv2.presentation.auth

sealed interface AuthEvent {

    data class EmailChanged(val email: String) : AuthEvent
    data class PasswordChanged(val password: String) : AuthEvent
    data class ConfirmationCodeChanged(val code: String) : AuthEvent

    data object SignIn : AuthEvent

    data object SignOut : AuthEvent

    data object SignUp : AuthEvent
    data class ConfirmSignUp(val email: String) : AuthEvent
    data class ResendConfirmationCode(val email: String) : AuthEvent

}