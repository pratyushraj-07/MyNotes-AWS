package com.example.mynotesv2.data.remote

import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.example.mynotesv2.domain.model.Note
import com.amplifyframework.datastore.generated.model.Note as AmplifyNote
import com.amplifyframework.kotlin.core.Amplify

class AWSNoteDataSourceImpl: AWSNoteDataSource {

    override suspend fun fetchNotes(): List<Note> {
        val result = Amplify.API.query(
            ModelQuery.list(AmplifyNote::class.java)
        )

        if(result.hasData()){
            val downloadedNotes =  mutableListOf<Note>()

            result.data.forEach{awsNote->
                val domainNote = Note(
                    id =awsNote.id,
                    title = awsNote.title,
                    description = awsNote.description,
                    timestamp = System.currentTimeMillis(),
                    isSynced = true
                )
                downloadedNotes.add(domainNote)
            }
            return downloadedNotes
        }else{
            throw Exception("Failed to fetch notes from cloud")
        }
    }

    override suspend fun createNote(note: Note) {
        val awsNote = AmplifyNote.builder()
            .title(note.title)
            .description(note.description)
            .id(note.id)
            .build()

        val result = Amplify.API.mutate(
            ModelMutation.create(awsNote)
        )

        if(result.hasData()){
            println("Note uploaded successfully")
        }else{
            throw Exception("Failed to upload note")
        }
    }

    override suspend fun updateNote(note: Note) {
        val awsNote = AmplifyNote.builder()
            .title(note.title)
            .description(note.description)
            .id(note.id)
            .build()

        val result = Amplify.API.mutate(
            ModelMutation.update(awsNote)
        )

        if(result.hasData()){
            println("Note updated successfully")
        }else{
            throw Exception("Failed to update note")
        }
    }

    override suspend fun deleteNote(noteId: String) {
        val noteToDelete = AmplifyNote.justId(noteId)

        val result = Amplify.API.mutate(
            ModelMutation.delete(noteToDelete)
        )

        if(result.hasData()){
            println("Note deleted successfully")
        }else{
            throw Exception("Failed to delete note")
        }
    }
}