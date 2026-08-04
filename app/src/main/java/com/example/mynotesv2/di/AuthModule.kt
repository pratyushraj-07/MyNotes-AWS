package com.example.mynotesv2.di

import com.example.mynotesv2.data.repository.AuthRepositoryImpl
import com.example.mynotesv2.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository() : AuthRepository{
        return AuthRepositoryImpl()
    }
}