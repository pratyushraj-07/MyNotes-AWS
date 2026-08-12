package com.example.mynotesv2.presentation.auth.signUp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mynotesv2.presentation.auth.AuthEvent
import com.example.mynotesv2.presentation.auth.AuthState
import com.example.mynotesv2.presentation.auth.AuthUiEvent
import com.example.mynotesv2.presentation.auth.AuthViewModel
import com.example.mynotesv2.presentation.nav_graph.Routes

@Composable
fun ConfirmSignUpRoute(
    navController: NavController,
    email:String,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                AuthUiEvent.NavigateToHome -> {
                    navController.navigate(Routes.NotesScreen.route) {
                        popUpTo(Routes.ConfirmSignUp.route) {
                            inclusive = true
                        }
                    }
                }

                is AuthUiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else-> {}
            }
        }
    }

    ConfirmSignUpScreen(
        state = state,
        onEvent = viewModel::onEvent,
        email = email
    )
}

@Composable
fun ConfirmSignUpScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    email: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Verify Your Email",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter the verification code sent to your email.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.confirmationCode,
            onValueChange = {
                onEvent(AuthEvent.ConfirmationCodeChanged(it))
            },
            label = {
                Text("Verification Code")
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (state.isLoading) {
            CircularProgressIndicator()

        } else {
            Button(
                onClick = {
                    onEvent(
                        AuthEvent.ConfirmSignUp(email)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm")
            }

            TextButton(
                onClick = {
                    onEvent(
                        AuthEvent.ResendConfirmationCode(email)
                    )
                }
            ) {
                Text("Resend Code")
            }
        }
    }
}