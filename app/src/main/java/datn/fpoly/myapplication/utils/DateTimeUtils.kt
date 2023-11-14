package datn.fpoly.myapplication.utils

import android.annotation.SuppressLint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    @SuppressLint("NewApi")
    fun formatDateOrder(inputDateTime: String): String {
        val instant = Instant.parse(inputDateTime)
        val outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
        return outputFormatter.format(instant.atZone(ZoneId.systemDefault()))
    }
}