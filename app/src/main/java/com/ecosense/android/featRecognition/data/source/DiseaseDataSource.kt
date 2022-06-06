package com.ecosense.android.featRecognition.data.source

import android.content.Context
import com.ecosense.android.R
import com.ecosense.android.featRecognition.data.model.DiseasesDto
import com.ecosense.android.featRecognition.domain.model.Disease
import com.google.gson.GsonBuilder
import logcat.asLog
import logcat.logcat

class DiseaseDataSource(
    appContext: Context
) {
    private val diseasesList = mutableListOf<Disease>()

    init {
        try {
            val inputStream = appContext.resources.openRawResource(R.raw.diseases)
            val json = inputStream.bufferedReader().use { it.readText() }

            val diseases = GsonBuilder()
                .create()
                .fromJson(json, DiseasesDto::class.java)
                .map { it.toDisease() }

            diseasesList.addAll(diseases)

            inputStream.close()
        } catch (e: Exception) {
            logcat { e.asLog() }
        }
    }

    fun getDisease(label: String): Disease? {
        return diseasesList.find { it.label == label }
    }
}