package com.example.mynotesv2.presentation.add_edit_note

data class AddEditState(
    val noteId: Long? = null,
    val title: String = "",
    val description : String = ""
)