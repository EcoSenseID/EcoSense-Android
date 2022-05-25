package com.ecosense.android.featDiscoverCampaign.data.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun dateFormatter(currentDate: String): String {
    val dateTime: ZonedDateTime = OffsetDateTime.parse(currentDate).toZonedDateTime()
    val systemZoneTime: ZonedDateTime = dateTime.withZoneSameInstant(ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return systemZoneTime.format(formatter)
}