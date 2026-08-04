package com.example.mynotesv2.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotesv2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<AuthUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event:AuthEvent){

        when(event){
            is AuthEvent.EmailChanged -> {
                _state.update {
                    it.copy(email = event.email)
                }
            }

            is AuthEvent.PasswordChanged -> {
                _state.update {
                    it.copy( password = event.password )
                }
            }

            is AuthEvent.ConfirmationCodeChanged -> {
                _state.update {
                    it.copy(confirmationCode = event.code)
                }
            }

            AuthEvent.SignIn -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    val result = authRepository.signIn(
                        email = _state.value.email,
                        password = _state.value.password
                    )

                    _state.update { it.copy(isLoading = false) }

                    result.onSuccess {
                        _uiEvent.send(AuthUiEvent.NavigateToHome)
                        Log.i("Success", "Login successful")
                    }.onFailure {
                        _uiEvent.send(AuthUiEvent.ShowToast(it.message ?: "Unknown Error"))
                    }
                }
            }

            AuthEvent.SignOut -> {
                viewModelScope.launch {
                    authRepository.signOut()
                    //toggle isLoggedIn to false
                    _uiEvent.send(AuthUiEvent.NavigateToHome)
                }
            }

            AuthEvent.SignUp -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true)}

                    val result = authRepository.signUp(
                        email = _state.value.email,
                        password = _state.value.password
                    )

                    _state.update { it.copy(isLoading = false) }

                    result.onSuccess {signUpResult->
                        if(signUpResult.isSignUpComplete){

                            _uiEvent.send(AuthUiEvent.NavigateToSignIn)

                        }else{ _uiEvent.send(AuthUiEvent.NavigateToConfirmSignUp(state.value.email)) }

                    }.onFailure {
                        _uiEvent.send(AuthUiEvent.ShowToast(it.message?:"Try Again"))
                    }
                }
            }

            is AuthEvent.ConfirmSignUp -> {
                viewModelScope.launch {
                    val result = authRepository.confirmSignUp(
                        email = event.email ,
                        code = state.value.confirmationCode
                    )
                    result.onSuccess {
                        _uiEvent.send(AuthUiEvent.NavigateToHome)
                    }.onFailure {
                        _uiEvent.send(
                            AuthUiEvent.ShowToast(it.message ?: "Try Again")
                        )
                    }
                }
                Log.d("Confirm", "Email = ${event.email}")
                Log.d("Confirm", "Code = ${state.value.confirmationCode}")

            }

            is AuthEvent.ResendConfirmationCode -> {
                viewModelScope.launch {

                    _state.update { it.copy(isLoading = true) }

                    val result = authRepository.resendConfirmationCode(
                        event.email
                    )

                    _state.update { it.copy(isLoading = false) }
                    result.onSuccess {
                        _uiEvent.send(
                            AuthUiEvent.ShowToast("Verification code sent")
                        )
                    }.onFailure {
                        _uiEvent.send(
                            AuthUiEvent.ShowToast(
                                it.message ?: "Try Again"
                            )
                        )
                    }
                }
            }
        }
    }
}