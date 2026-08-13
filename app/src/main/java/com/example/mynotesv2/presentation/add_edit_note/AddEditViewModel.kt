package com.example.mynotesv2.presentation.add_edit_note

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotesv2.domain.model.Note
import com.example.mynotesv2.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(AddEditState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<AddEditUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val noteId = savedStateHandle.get<Long>("noteId")
        if(noteId != null && noteId != -1L){
            viewModelScope.launch {
                val note = repository.getNotesById(noteId)
                if (note != null) {
                    _state.update {
                        it.copy(
                            noteId = note.id,
                            title = note.title,
                            description = note.description
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(title:String){
        _state.update { it.copy(title = title) }
    }

    fun onDescriptionChange(description:String){
        _state.update { it.copy(description = description) }
    }

    fun saveNote(){
        if (_state.value.isSaving) return

        _state.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            try{
                val error = validateNote()
                if (error != null) {
                    _uiEvent.send(AddEditUiEvent.ShowSnackBar(error))
                    return@launch
                }

                val note = Note(
                    id = state.value.noteId ?: 0L,
                    title = state.value.title,
                    description = state.value.description,
                    timestamp = System.currentTimeMillis()
                )

                repository.insertNote(note = note)
                _uiEvent.send(AddEditUiEvent.NavigateBack)
            }finally {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun validateNote(): String?{
        return if(state.value.title.isBlank()){
            "Title cannot be empty"
        }else{
            null
        }
    }
}