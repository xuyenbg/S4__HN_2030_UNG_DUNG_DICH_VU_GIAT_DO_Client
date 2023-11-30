package datn.fpoly.myapplication.utils

import android.annotation.SuppressLint
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object Utils {
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

    fun formatVND(price: Double): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val formattedAmount = currencyFormat.format(price)
        return formattedAmount
    }
}