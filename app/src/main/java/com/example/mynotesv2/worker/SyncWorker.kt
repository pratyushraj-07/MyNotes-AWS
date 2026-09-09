package com.example.mynotesv2.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mynotesv2.domain.repository.NoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: NoteRepository
): CoroutineWorker(appContext, workerParams){

    override suspend fun doWork(): Result {
        return try {
            Result.success()
        }catch (e:Exception){
            Result.retry()
        }
    }
}