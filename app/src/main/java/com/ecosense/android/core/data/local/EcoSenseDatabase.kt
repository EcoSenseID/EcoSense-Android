package com.ecosense.android.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ecosense.android.core.data.local.dao.SavedRecognisableDao
import com.ecosense.android.core.data.model.SavedRecognisableEntity

@Database(
    entities = [SavedRecognisableEntity::class],
    version = 1
)
abstract class EcoSenseDatabase : RoomDatabase() {
    abstract val savedRecognisableDao: SavedRecognisableDao

    companion object {
        const val NAME = "db_ecosense"
    }
}