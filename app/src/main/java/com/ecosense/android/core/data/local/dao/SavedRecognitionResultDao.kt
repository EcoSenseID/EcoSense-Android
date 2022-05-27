package com.ecosense.android.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ecosense.android.featRecognition.data.model.SavedRecognitionResultEntity

@Dao
interface SavedRecognitionResultDao {
    @Query("SELECT * FROM savedrecognitionresultentity")
    suspend fun findAll(): List<SavedRecognitionResultEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun save(recognitionResult: SavedRecognitionResultEntity)

    @Query("DELETE FROM savedrecognitionresultentity WHERE id = :id")
    suspend fun delete(id: Int?)

    @Query("SELECT * FROM savedrecognitionresultentity WHERE id = :id")
    suspend fun find(id: Int): SavedRecognitionResultEntity?
}