package com.example.mynotesv2.presentation.add_edit_note

sealed interface AddEditUiEvent {
    data object NavigateBack : AddEditUiEvent
    data class ShowSnackBar(val message:String) : AddEditUiEvent
}