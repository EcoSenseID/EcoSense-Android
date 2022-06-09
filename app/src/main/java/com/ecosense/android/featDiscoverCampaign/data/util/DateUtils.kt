package com.ecosense.android.featDiscoverCampaign.data.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.math.ceil

@SuppressLint("SimpleDateFormat")
fun dateFormatter(date: String): String {
    return if (date == "") {
        ""
    } else {
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        val netDate = Date(date.toLong() * 1000)
        sdf.format(netDate)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun countDays(date: String) : String {
    val start = LocalDateTime.now()
    val end = Instant.ofEpochSecond(date.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    val count = Duration.between(start, end).toHours().toDouble()
    val result = ceil(count / 24)

    return result.toInt().toString()
}