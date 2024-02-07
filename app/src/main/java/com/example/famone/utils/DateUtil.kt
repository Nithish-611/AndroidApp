package com.example.famone.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object DateUtil {

    @SuppressLint("SimpleDateFormat")
    fun dateToMillis(dateString: String?): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date: Date? = dateString?.let { sdf.parse(it) }
            date?.time?:0L
        } catch (e: ParseException) {
            e.printStackTrace()
            0L
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun millisToDate(millis: Long?): String {
        if(millis != null && millis != 0L){
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(Date(millis))
        }
        return "Set Reminder"
    }
}