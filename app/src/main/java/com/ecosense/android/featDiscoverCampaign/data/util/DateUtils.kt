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
import kotlin.math.floor

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

fun unixCountdown(date: String): String {
    val currentTime = System.currentTimeMillis() / 1000

    val start = currentTime.toDouble()
    val end = date.toDouble()

    // get total seconds between the times
    var delta = end - start

    // calculate (and subtract) whole days
    val days = floor(delta / 86400)
    delta -= days * 86400

    // calculate (and subtract) whole hours
    val hours = floor(delta / 3600) % 24
    delta -= hours * 3600

    // calculate (and subtract) whole minutes
    val minutes = floor(delta / 60) % 60
    delta -= minutes * 60

    // what's left is seconds
    val seconds = floor(delta % 60)  // in theory the modulus is not required

    return "${days.toInt()} Days ${hours.toInt()} Hrs ${minutes.toInt()} Mins ${seconds.toInt()} Secs"
}