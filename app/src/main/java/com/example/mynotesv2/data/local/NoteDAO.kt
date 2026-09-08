package com.example.mynotesv2.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDAO {

    @Query("SELECT * FROM notes WHERE syncState!='PENDING_DELETE' ORDER BY timeStamp DESC")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes where id = :id")
    suspend fun getNoteById(id:String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note:NoteEntity)

    @Delete
    suspend fun deleteNote(note:NoteEntity)

    @Query("SELECT * FROM notes where syncState!='SYNCED'")
    suspend fun getUnSyncedNotes(): List<NoteEntity>

}