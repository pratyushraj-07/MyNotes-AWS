package com.example.mynotesv2.domain.repository

import com.example.mynotesv2.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes() : Flow<List<Note>>

    suspend fun getNotesById(id: Long) : Note?

    suspend fun insertNote(note:Note)

    suspend fun deleteNote(note:Note)

}
