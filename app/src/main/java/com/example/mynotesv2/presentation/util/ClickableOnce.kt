package com.example.mynotesv2.presentation.util

import android.os.SystemClock
import androidx.compose.runtime.*

@Composable
fun rememberClickOnce(
    intervalMs: Long = 500L,
    onClick: () -> Unit
): () -> Unit{
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return {
        val currentTime = SystemClock.elapsedRealtime()

        if(lastClickTime == 0L || currentTime - lastClickTime > intervalMs){
            lastClickTime = currentTime
            onClick()
        }
    }
}