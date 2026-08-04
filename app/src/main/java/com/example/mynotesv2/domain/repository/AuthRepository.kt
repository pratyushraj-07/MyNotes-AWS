package com.example.mynotesv2.domain.repository

import com.amplifyframework.auth.result.AuthSignUpResult
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    suspend fun signUp(email: String, password: String): Result<AuthSignUpResult>
    suspend fun confirmSignUp(email: String, code: String): Result<Unit>

    suspend fun signIn(email: String, password: String): Result<Unit>

    suspend fun signOut()

    val authState: StateFlow<Boolean>
    suspend fun refreshAuthState()

    suspend fun resendConfirmationCode(email: String): Result<Unit>

}