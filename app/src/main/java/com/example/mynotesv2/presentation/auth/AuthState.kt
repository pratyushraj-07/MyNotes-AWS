package com.example.mynotesv2.presentation.auth

data class AuthState(

    val email: String = "",
    val password: String = "",
    val confirmationCode: String = "",
    val isLoading: Boolean = false

)