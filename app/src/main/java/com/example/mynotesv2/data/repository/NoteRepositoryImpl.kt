package com.example.mynotesv2.data.repository

import com.example.mynotesv2.data.local.NoteDAO
import com.example.mynotesv2.data.local.NoteEntity
import com.example.mynotesv2.data.remote.AWSNoteDataSource
import com.example.mynotesv2.domain.model.Note
import com.example.mynotesv2.domain.model.SyncState
import com.example.mynotesv2.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDAO,
    private val awsDataNoteSource: AWSNoteDataSource
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
        val existingNote = dao.getNoteById(note.id)

        val finalState = if(existingNote!=null){
            if (existingNote.syncState == SyncState.SYNCED) {
                SyncState.PENDING_UPDATE
            } else {
                existingNote.syncState
            }
        }else{
            SyncState.PENDING_CREATE
        }

        dao.insertNote(note.toEntity().copy(syncState = finalState))
    }

    override suspend fun deleteNote(note: Note) {
        val existingNote = dao.getNoteById(note.id) ?: return

        if (existingNote.syncState == SyncState.PENDING_CREATE) {
            dao.deleteNote(existingNote)
        } else {
            val softDeletedNote = existingNote.copy(syncState = SyncState.PENDING_DELETE)
            dao.insertNote(softDeletedNote)
        }
    }

    override suspend fun getUnSyncedNotes(): List<Note> {
        return dao.getUnSyncedNotes().map { it.toNote() }
    }

    override suspend fun pushUnSyncedNote() {
        val unSyncedNotes = getUnSyncedNotes()

        for(note in unSyncedNotes){
            try {
                when(note.syncState){
                    SyncState.SYNCED -> {}
                    SyncState.PENDING_CREATE -> {
                        awsDataNoteSource.createNote(note)
                        dao.insertNote(note.toEntity().copy(syncState = SyncState.SYNCED))
                    }
                    SyncState.PENDING_UPDATE -> {
                        awsDataNoteSource.updateNote(note)
                        dao.insertNote(note.toEntity().copy(syncState = SyncState.SYNCED))
                    }
                    SyncState.PENDING_DELETE -> {
                        awsDataNoteSource.deleteNote(note.id)
                        dao.deleteNote(note.toEntity())
                    }
                }

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    override suspend fun pullNotesFromCloud() {
        try {
            val cloudNotes = awsDataNoteSource.fetchNotes()

            for (note in cloudNotes){
                dao.insertNote(note.toEntity().copy(syncState = SyncState.SYNCED))
            }
        }catch (e:Exception){
            e.printStackTrace()
            throw e
        }
    }
}

fun NoteEntity.toNote() = Note(
    id = this.id,
    title = this.title,
    description = this.description,
    timestamp = this.timeStamp,
    syncState = this.syncState
)

fun Note.toEntity() = NoteEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    timeStamp = this.timestamp,
    syncState = this.syncState
)