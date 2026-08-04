package com.example.mynotesv2.presentation.auth.signUp

import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mynotesv2.presentation.auth.AuthEvent
import com.example.mynotesv2.presentation.auth.AuthState
import com.example.mynotesv2.presentation.auth.AuthUiEvent
import com.example.mynotesv2.presentation.auth.AuthViewModel
import com.example.mynotesv2.presentation.nav_graph.Routes

@Composable
fun SignUpRoute(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
){

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                AuthUiEvent.NavigateToHome -> {
                    navController.navigate(Routes.NotesScreen.route){
                        popUpTo(Routes.SignUpScreen.route){
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

                is AuthUiEvent.NavigateToConfirmSignUp -> {
                    navController.navigate(Routes.ConfirmSignUp.createRoute(event.email))
                }
                AuthUiEvent.NavigateToSignIn -> {
                    navController.navigate(Routes.SignInScreen.route)
                }
            }
        }
    }

    SignUpScreen(
        state = state,
        onEvent = viewModel::onEvent
    )

}

@Composable
fun SignUpScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onEvent(AuthEvent.SignUp) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
        }
    }
}