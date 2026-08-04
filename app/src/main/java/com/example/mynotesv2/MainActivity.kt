package com.example.mynotesv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mynotesv2.presentation.nav_graph.NavGraph
import com.example.mynotesv2.presentation.note.NotesRoute
import com.example.mynotesv2.presentation.note.NotesScreen
import com.example.mynotesv2.presentation.note.NotesViewModel
import com.example.mynotesv2.ui.theme.MyNotesV2Theme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyNotesV2Theme {

                val navController = rememberNavController()
                NavGraph(navController = navController)

            }
        }
    }
}