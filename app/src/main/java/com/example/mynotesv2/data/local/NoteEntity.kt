package com.example.mynotesv2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynotesv2.domain.model.SyncState

@Entity(tableName = "notes")
data class NoteEntity (

    @PrimaryKey
    val id: String,

    val title:String,
    val description:String,
    val timeStamp: Long,
    val syncState: SyncState = SyncState.PENDING_CREATE
)