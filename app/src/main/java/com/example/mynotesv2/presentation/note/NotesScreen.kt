package com.example.mynotesv2.presentation.note

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.mynotesv2.R
import com.example.mynotesv2.domain.model.Note
import com.example.mynotesv2.presentation.nav_graph.Routes

@Composable
fun NotesRoute(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isUserSignedIn by viewModel.isUserSignedIn.collectAsState()

    NotesScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNoteClick = { note ->
            navController.navigate(Routes.AddEditScreen.route + "?noteId=${note.id}")
        },
        onAddClick = { navController.navigate(Routes.AddEditScreen.route) },
        onSignInCLick = {navController.navigate(Routes.SignInScreen.route)},
        onSignUpCLick = {navController.navigate(Routes.SignUpScreen.route)},
        isLoggedIn = isUserSignedIn
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    state: NotesState,
    onEvent: (NotesEvent) -> Unit,
    onAddClick: () -> Unit,
    onNoteClick: (note: Note) -> Unit,
    onSignInCLick: () -> Unit,
    onSignUpCLick:() -> Unit,
    isLoggedIn: Boolean
) {

    var expanded by remember{ mutableStateOf( false ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MyNotes",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
                    titleContentColor = Color.White
                ),
                actions = {
                    Box {
                        IconButton(
                            onClick = { expanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Login"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            if (isLoggedIn) {
                                DropdownMenuItem(
                                    text = { Text("Sync") },
                                    onClick = {
                                        expanded = false
                                       // onSyncClick()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Sign Out") },
                                    onClick = {
                                        onEvent(NotesEvent.SignOut)
                                        expanded = false
                                    }
                                )
                            } else {
                                DropdownMenuItem(
                                    text = { Text("Sign In") },
                                    onClick = {
                                        expanded = false
                                        onSignInCLick()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Sign Up") },
                                    onClick = {
                                        expanded = false
                                        onSignUpCLick()
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        containerColor = Color.DarkGray,
        contentColor = MaterialTheme.colorScheme.onBackground,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                elevation = FloatingActionButtonDefaults.elevation(6.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
                start = 10.dp,
                end = 10.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.notes.isNotEmpty()) {
                items(
                    state.notes,
                    key = { it.id }
                ) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        ),
                        onNoteClick = { onNoteClick(note) },
                        onDeleteClick = { onEvent(NotesEvent.DeleteNote(note = note)) }
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.fillParentMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_state))

                        LottieAnimation(
                            composition = composition,
                            modifier = Modifier.size(250.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Write Your Thoughts",
                            fontSize = 20.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}