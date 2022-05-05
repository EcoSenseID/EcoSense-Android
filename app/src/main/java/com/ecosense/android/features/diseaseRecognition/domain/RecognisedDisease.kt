package com.ecosense.android.features.diseaseRecognition.domain

data class RecognisedDisease(val label: String, val confidence: Float) {
    private val probabilityString = String.format("%.1f%%", confidence * 100.0f)

    override fun toString(): String {
        return "$label / $probabilityString"
    }
}