package com.ecosense.android.featRecognition.data.repository

import android.graphics.Bitmap
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featRecognition.domain.model.Disease
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRecognitionRepository(
    private val fakeRecogniseResult: List<Recognisable>? = null,
    private val fakeSavedRecognisables: Resource<List<SavedRecognisable>>? = null,
    private val fakeDisease: Disease? = null,
    private val fakeSaveRecognisableResult: Resource<Int>? = null,
) : RecognitionRepository {
    override fun recognise(
        bitmap: Bitmap
    ): List<Recognisable> {
        return fakeRecogniseResult ?: throw NullPointerException()
    }

    override fun getSavedRecognisables(): Flow<Resource<List<SavedRecognisable>>> = flow {
        emit(fakeSavedRecognisables ?: throw NullPointerException())
    }

    override fun getDisease(label: String): Disease? {
        return fakeDisease
    }

    override suspend fun saveRecognisable(
        label: String,
        confidencePercent: Int
    ): Flow<Resource<Int>> = flow {
        emit(fakeSaveRecognisableResult ?: throw NullPointerException())
    }
}