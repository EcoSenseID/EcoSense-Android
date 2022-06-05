package com.ecosense.android.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ecosense.android.core.data.model.SavedRecognisableEntity

@Dao
interface SavedRecognisableDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun save(recognitionResult: SavedRecognisableEntity): Long

    @Query("SELECT * FROM savedrecognisableentity WHERE id = :id")
    suspend fun find(id: Int): SavedRecognisableEntity?

    @Query("SELECT * FROM savedrecognisableentity")
    suspend fun findAll(): List<SavedRecognisableEntity>

    @Query("DELETE FROM savedrecognisableentity WHERE id = :id")
    suspend fun delete(id: Int?): Int
}