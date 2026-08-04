package com.example.mynotesv2.data.repository

import android.util.Log
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSignUpResult
import com.amplifyframework.kotlin.core.Amplify
import com.example.mynotesv2.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryImpl : AuthRepository {

    private val _authState = MutableStateFlow(false)
    override val authState = _authState.asStateFlow()

    override suspend fun refreshAuthState() {
        _authState.value =  try {
            Amplify.Auth.getCurrentUser()
            true
        }catch (e:Exception){
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Result<AuthSignUpResult> {
        return try {
            val result = Amplify.Auth.signUp(
                username = email,
                password = password,
                options = AuthSignUpOptions.builder()
                    .userAttribute(
                        AuthUserAttributeKey.email(),
                        email
                    )
                    .build()
            )

            Result.success(result)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun confirmSignUp(email: String, code: String): Result<Unit> {
        return try {
            Log.d("Confirm", "Email: $email")
            Log.d("Confirm", "Code: $code")

            Amplify.Auth.confirmSignUp(
                username = email,
                confirmationCode = code
            )

            _authState.value = true
            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            val result = Amplify.Auth.signIn(
                username = email,
                password = password,
                options = AuthSignInOptions.defaults()
            )
            if (result.isSignedIn){
                _authState.value = true
                Result.success(Unit)
            }else{
                Result.success(Unit)
            }
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        try {
            Amplify.Auth.signOut()
            _authState.value = false
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun resendConfirmationCode(email: String): Result<Unit> {
        return try {
            val result = Amplify.Auth.resendSignUpCode(
                username = email
            )
            Log.d("ResendCode", "Success! Code sent to: ${result.destination}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ResendCode", "Failed to resend code!", e)
            Result.failure(e)

        }
    }
}