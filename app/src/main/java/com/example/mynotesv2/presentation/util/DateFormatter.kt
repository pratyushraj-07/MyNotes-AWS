package com.example.mynotesv2.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(): String{
    return SimpleDateFormat(
        "MMM d, yyyy",
        Locale.getDefault()
    ).format(Date(this))
}