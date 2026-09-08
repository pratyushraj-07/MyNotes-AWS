package com.example.mynotesv2.data.local

import androidx.room.TypeConverter
import com.example.mynotesv2.domain.model.SyncState

class NoteTypeConverter {
    @TypeConverter
    fun fromSyncState(value:SyncState):String{
        return value.name
    }

    @TypeConverter
    fun toSyncState(value:String): SyncState{
        return SyncState.valueOf(value)
    }
}