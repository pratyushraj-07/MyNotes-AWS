package com.example.mynotesv2.data.remote

import com.example.mynotesv2.domain.model.Note

interface AWSNoteDataSource {
    suspend fun fetchNotes(): List<Note>
    suspend fun createNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(noteId: String)
}