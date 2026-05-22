package com.spendwise.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateMoneyUtils {
    private val displayDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    fun formatDate(millis: Long): String = displayDate.format(Date(millis))

    fun formatMoney(amount: Double, symbol: String): String = "$symbol${NumberFormat.getNumberInstance().format(amount)}"

    fun monthKey(time: Long = System.currentTimeMillis()): String {
        val cal = Calendar.getInstance().apply { this.timeInMillis = time }
        return "${cal.get(Calendar.YEAR)}-${(cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')}"
    }

    fun monthBounds(time: Long = System.currentTimeMillis()): Pair<Long, Long> {
        val start = Calendar.getInstance().apply {
            timeInMillis = time
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val end = start.clone() as Calendar
        end.add(Calendar.MONTH, 1)
        end.add(Calendar.MILLISECOND, -1)
        return start.timeInMillis to end.timeInMillis
    }
}
