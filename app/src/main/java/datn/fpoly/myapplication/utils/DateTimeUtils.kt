package datn.fpoly.myapplication.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekByMonth(): Int {
        val currentDate = LocalDate.now()

        // Lấy trường tuần của ngày hiện tại
        val weekFields = WeekFields.of(Locale.getDefault())
        return  currentDate.get(weekFields.weekOfMonth())
    }

    fun formatVND(price: Double): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val formattedAmount = currencyFormat.format(price)
        return formattedAmount
    }
}