package com.ecosense.android.featReward.data.util

import java.text.NumberFormat
import java.util.*

fun ecopointsFormatter(ecopoints: Int): String {
    return NumberFormat.getNumberInstance(Locale.US)
        .format(ecopoints)
        .replace(",", ".")
}