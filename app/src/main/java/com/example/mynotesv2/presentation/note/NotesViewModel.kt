package com.example.mynotesv2.presentation.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mynotesv2.domain.repository.AuthRepository
import com.example.mynotesv2.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    val isUserSignedIn = authRepository.authState

    init {
        viewModelScope.launch {
            authRepository.refreshAuthState()
        }
    }

    val state = repository.getAllNotes().map { note->
         NotesState(notes = note)
     }.stateIn(
         viewModelScope,
         started = SharingStarted.WhileSubscribed(5000L),
         initialValue = NotesState(emptyList())
     )

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    repository.deleteNote(event.note)
                }
            }

            NotesEvent.SignOut -> {
                viewModelScope.launch {
                    authRepository.signOut()
                }
            }
        }
    }
}

//private val _isUserLoggedIn = MutableStateFlow(false)
//
//init {
//    refreshLoginState()
//}
//private fun refreshLoginState() {
//    viewModelScope.launch {
//        _isUserLoggedIn.value = authRepository.isUserLoggedIn()
//    }
//}

//val state = combine(
//    repository.getAllNotes(),_isUserLoggedIn
//){notes, isLoggedIn->
//    NotesState(
//        notes = notes,
//        isUserLoggedIn = isLoggedIn
//    )
//}.stateIn(
//    viewModelScope,
//    started = SharingStarted.WhileSubscribed(5000L),
//    initialValue = NotesState()
//)