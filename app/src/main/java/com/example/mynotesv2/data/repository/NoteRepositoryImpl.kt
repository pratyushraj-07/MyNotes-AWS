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

    override suspend fun getNotesById(id: String): Note? {
        return dao.getNoteById(id)?.toNote()
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toEntity())
    }

    override suspend fun getUnSyncedNotes(): List<Note> {
        return dao.getUnSyncedNotes().map { it.toNote() }
    }
}

fun NoteEntity.toNote() = Note(
    id = this.id,
    title = this.title,
    description = this.description,
    timestamp = this.timeStamp,
    isSynced = this.isSynced
)

fun Note.toEntity() = NoteEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    timeStamp = this.timestamp,
    isSynced = this.isSynced
)