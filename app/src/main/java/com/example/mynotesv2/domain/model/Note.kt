package com.example.mynotesv2.domain.model

data class Note(
    val id:Long ,
    val title: String,
    val description: String,
    val timestamp: Long
)
