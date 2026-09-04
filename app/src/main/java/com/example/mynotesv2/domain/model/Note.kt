package com.example.mynotesv2.domain.model

import java.util.UUID

data class Note(
    val id:String = UUID.randomUUID().toString() ,
    val title: String,
    val description: String,
    val timestamp: Long,
    val isSynced:Boolean = false
)
