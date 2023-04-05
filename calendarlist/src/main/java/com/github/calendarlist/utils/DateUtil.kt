package com.github.calendarlist.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        fun Long.parse(pattern: String = "EEEE, d MMMM yyyy") : String {
            return try {
                val sbf = SimpleDateFormat(pattern, Locale("id", "ID"))
                val netDate = Date(this)
                sbf.format(netDate)
            } catch (e: Exception) {
                ""
            }
        }
    }
}