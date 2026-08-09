package com.example.mynotesv2.presentation.nav_graph

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mynotesv2.presentation.add_edit_note.AddEditRoute
import com.example.mynotesv2.presentation.auth.signIn.SignInRoute
import com.example.mynotesv2.presentation.auth.signUp.ConfirmSignUpRoute
import com.example.mynotesv2.presentation.auth.signUp.SignUpRoute
import com.example.mynotesv2.presentation.note.NotesRoute

@Composable
fun NavGraph(
    navController:NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.NotesScreen.route,
        modifier = Modifier.background(Color.DarkGray),
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) },
        popEnterTransition = {slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))},
        exitTransition = {slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))}
    ) {
        composable(
            route = Routes.NotesScreen.route,
        ){
            NotesRoute(navController = navController)
        }
        composable(
            route = Routes.AddEditScreen.route + "?noteId={noteId}",
            arguments = listOf(
                navArgument("noteId"){
                    type = NavType.LongType
                    defaultValue = -1L
                },
            )
        ) {
            AddEditRoute(navController = navController)
        }

        composable(route = Routes.SignInScreen.route){
            SignInRoute(navController)
        }

        composable(route = Routes.SignUpScreen.route) {
            SignUpRoute(navController)
        }


        composable(
            route = Routes.ConfirmSignUp.route,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val email = backStackEntry.arguments?.getString("email") ?: ""

            Log.d("NavArgument", "Email received = $email")

            ConfirmSignUpRoute(
                navController = navController,
                email = email
            )
        }
    }
}