package com.ecosense.android.featRecognition.data.remote

import com.ecosense.android.featRecognition.data.model.SaveRecognisableDto
import com.ecosense.android.featRecognition.data.model.SavedRecognisablesDto
import retrofit2.http.*

interface RecognitionApi {
    @GET("savedrecognisables")
    suspend fun getSavedRecognisables(
        @Header("Authorization") bearerToken: String,
    ): SavedRecognisablesDto

    @FormUrlEncoded
    @POST("saverecognisable")
    suspend fun saveRecognisable(
        @Header("Authorization") bearerToken: String,
        @Field("label") label: String,
        @Field("confidencePercent") confidencePercent: Int,
    ):SaveRecognisableDto
}