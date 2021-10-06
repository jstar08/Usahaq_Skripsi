package com.example.usahaq_skripsi.util

import java.text.SimpleDateFormat
import java.util.*

class DateHelper {
    companion object {
        fun getCurrentDate(): String {
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateInstance()
            return formatter.format(date)
        }

        fun getNextDate(day: Int): String {
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_YEAR, day)
            val time = date.time
            val formatter = SimpleDateFormat.getDateInstance()
            return formatter.format(time).split(",")[0]
        }

        fun getDayName(day : Int) : String {
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_YEAR, day)
            var days = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
            return days[date.get(Calendar.DAY_OF_WEEK)-1]
        }

    }
}