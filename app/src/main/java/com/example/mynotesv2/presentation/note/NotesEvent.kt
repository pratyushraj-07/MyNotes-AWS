package com.example.mynotesv2.presentation.note

import com.example.mynotesv2.domain.model.Note


sealed interface NotesEvent{

    data class DeleteNote(val note: Note) : NotesEvent
    data object SignOut : NotesEvent

}