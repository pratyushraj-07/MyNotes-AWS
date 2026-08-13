package com.example.mynotesv2.presentation.util

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.clickableOnce(
    enabled: Boolean = true,
    intervalMs: Long = 500L,
    onClick: () -> Unit
):Modifier = composed{
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.clickable(enabled = enabled){
        val currentTime = SystemClock.elapsedRealtime()

        if(currentTime - lastClickTime > intervalMs){
            lastClickTime = currentTime
            onClick()
        }
    }
}

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