package com.example.medirem.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun formatToAmPm(time24h: String): String {
        return try {
            val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())
            val sdf12 = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val date = sdf24.parse(time24h)
            if (date != null) sdf12.format(date) else time24h
        } catch (e: Exception) {
            time24h
        }
    }
}