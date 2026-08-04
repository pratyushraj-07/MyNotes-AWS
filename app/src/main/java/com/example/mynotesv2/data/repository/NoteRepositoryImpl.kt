package com.example.mynotesv2.data.repository

import com.example.mynotesv2.data.local.NoteDAO
import com.example.mynotesv2.data.local.NoteEntity
import com.example.mynotesv2.domain.model.Note
import com.example.mynotesv2.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDAO
): NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getNotes().map { entities->
            entities.map { it.toNote() }
        }
    }

    override suspend fun getNotesById(id: Long): Note? {
        return dao.getNoteById(id)?.toNote()
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toEntity())
    }
}

fun NoteEntity.toNote() = Note(
    id = id,
    title = title,
    description = description,
    timestamp = timeStamp
)

fun Note.toEntity() = NoteEntity(
    id = id,
    title = title,
    description = description,
    timeStamp = timestamp
)