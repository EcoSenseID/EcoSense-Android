package com.ecosense.android.featDiscoverCampaign.data.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

@SuppressLint("SimpleDateFormat")
fun detailDateFormatter(date: String): String {
    return if (date == "") {
        ""
    } else {
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        val netDate = Date(date.toLong() * 1000)
        sdf.format(netDate)
    }
}

@SuppressLint("SimpleDateFormat")
fun dateFormatter(date: String): String {
    return if (date == "") {
        ""
    } else {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val netDate = Date(date.toLong() * 1000)
        sdf.format(netDate)
    }
}

@SuppressLint("SimpleDateFormat")
fun countDays(date: String) : String {
    return if (date == "") {
        ""
    } else {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        val destFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        sourceFormat.timeZone = utc
        val convertedDate = sourceFormat.parse(Calendar.getInstance().time.toString())
        val convertedDate2 =  destFormat.format(convertedDate)
        val currentDate = format.parse(convertedDate2)

        val endDate = format.parse(date)

        val days = (endDate.time - currentDate.time) / 3600 / 24

        days.toString()
    }
}

fun currentDateParser(date: String) : String {
    val utc = TimeZone.getTimeZone("UTC")
    val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
    val destFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    sourceFormat.timeZone = utc
    val convertedDate = sourceFormat.parse(date)
    return destFormat.format(convertedDate)
}