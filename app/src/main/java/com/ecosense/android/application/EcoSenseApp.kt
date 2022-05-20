package com.ecosense.android.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class EcoSenseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(
            application = this,
            minPriority = LogPriority.VERBOSE
        )
    }
}