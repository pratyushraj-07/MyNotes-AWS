package com.example.mynotesv2.presentation.nav_graph

import android.net.Uri

sealed class Routes(val route:String){
    data object NotesScreen: Routes(route = "notes")
    data object AddEditScreen: Routes(route = "add_edit")
    data object SignInScreen: Routes(route = "sign_in")
    data object SignUpScreen: Routes(route = "sign_up")

    data object ConfirmSignUp : Routes("confirm_signup/{email}") {
        fun createRoute(email: String): String {
            return "confirm_signup/${Uri.encode(email)}"
        }
    }

}