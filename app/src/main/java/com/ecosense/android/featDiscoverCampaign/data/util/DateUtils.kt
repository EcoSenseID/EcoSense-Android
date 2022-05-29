package com.ecosense.android.featDiscoverCampaign.data.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
fun dateFormatter(currentDate: String): String {
    val dateTime: ZonedDateTime = OffsetDateTime.parse(currentDate).toZonedDateTime()
    val systemZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return systemZoneTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun countDays(date: String) : String {
    val start = LocalDateTime.now()
    val end = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)

    val count = Duration.between(start, end).toHours().toDouble()
    val result = ceil(count / 24)

    return result.toInt().toString()
}