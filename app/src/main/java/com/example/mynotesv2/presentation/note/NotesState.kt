package com.example.mynotesv2.presentation.note

import com.example.mynotesv2.domain.model.Note

data class NotesState(
    val notes: List<Note> = emptyList()
)
