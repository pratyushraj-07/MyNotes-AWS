package com.example.mynotesv2.di

import com.example.mynotesv2.data.local.NoteDAO
import com.example.mynotesv2.data.repository.NoteRepositoryImpl
import com.example.mynotesv2.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideNoteRepository(
        dao:NoteDAO
    ): NoteRepository{
        return NoteRepositoryImpl(dao)
    }

}