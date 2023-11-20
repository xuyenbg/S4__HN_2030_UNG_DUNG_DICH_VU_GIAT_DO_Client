package datn.fpoly.myapplication.utils

import android.annotation.SuppressLint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    @SuppressLint("NewApi")
    fun formatDateOrder(inputDateTime: String): String {
        try {
            val instant = Instant.parse(inputDateTime)
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
            val outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
            return localDateTime.format(outputFormatter)
        } catch (e: Exception) {
            return ""
        }

    }
}