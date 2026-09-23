package com.example.mynotesv2.presentation.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotesv2.domain.repository.AuthRepository
import com.example.mynotesv2.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.refreshAuthState()
        }

        viewModelScope.launch(Dispatchers.IO) {
            isUserSignedIn
                .distinctUntilChanged{old, new->old == new}
                .collect{signedIn->
                if(signedIn){
                    try {
                        repository.pushUnSyncedNote()
                        repository.pullNotesFromCloud()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
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

    fun manualSync(){
        viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.value = true
            try {
                repository.pushUnSyncedNote()
                repository.pullNotesFromCloud()
            }catch (e:Exception){
                e.printStackTrace()
            }finally {
                _isRefreshing.value = false
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