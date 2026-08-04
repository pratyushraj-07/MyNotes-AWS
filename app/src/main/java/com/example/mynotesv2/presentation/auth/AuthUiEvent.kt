package com.example.mynotesv2.presentation.auth

sealed interface AuthUiEvent {

    data object NavigateToHome : AuthUiEvent
    data object NavigateToSignIn : AuthUiEvent
    data class NavigateToConfirmSignUp (val email:String): AuthUiEvent
    data class ShowToast(val message: String) : AuthUiEvent

}