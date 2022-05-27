package com.ecosense.android.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ecosense.android.core.data.local.dao.SavedRecognitionResultDao
import com.ecosense.android.featRecognition.data.model.SavedRecognitionResultEntity

@Database(
    entities = [SavedRecognitionResultEntity::class],
    version = 1
)
abstract class EcoSenseDatabase : RoomDatabase() {
    abstract val savedRecognitionResultDao: SavedRecognitionResultDao

    companion object {
        const val NAME = "db_ecosense"
    }
}