package com.ecosense.android.featRecognition.data.util

import com.ecosense.android.R
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.domain.model.RecognitionResult
import org.tensorflow.lite.support.label.Category

fun Category.toRecognisedDisease() = RecognitionResult(
    label = categoryLabelToUIText(this.label),
    confidencePercent = (this.score * 100).toInt()
)

private fun categoryLabelToUIText(
    label: String
): UIText = when (label) {
    "Apple___Apple_scab" -> UIText.StringResource(R.string.disease_apple_scab)
    "Apple___Black_rot" -> UIText.StringResource(R.string.disease_apple_black_rot)
    "Apple___Cedar_apple_rust" -> UIText.StringResource(R.string.disease_apple_cedar_apple_rust)
    "Apple___healthy" -> UIText.StringResource(R.string.disease_apple_healthy)
    "Blueberry___healthy" -> UIText.StringResource(R.string.disease_blueberry_healthy)
    "Cherry_(including_sour)___healthy" -> UIText.StringResource(R.string.disease_cherry_inc_sour_healthy)
    "Cherry_(including_sour)___Powdery_mildew" -> UIText.StringResource(R.string.disease_cherry_inc_sour_powdery_mildew)
    "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot" -> UIText.StringResource(R.string.disease_corn_maize_cercospora)
    "Corn_(maize)___Common_rust_" -> UIText.StringResource(R.string.disease_corn_maize_common_rust)
    "Corn_(maize)___healthy" -> UIText.StringResource(R.string.disease_corn_maize_healthy)
    "Corn_(maize)___Northern_Leaf_Blight" -> UIText.StringResource(R.string.disease_corn_maize_northern_leaf_blight)
    "Grape___Black_rot" -> UIText.StringResource(R.string.disease_grape_black_rot)
    "Grape___Esca_(Black_Measles)" -> UIText.StringResource(R.string.disease_grape_esca)
    "Grape___healthy" -> UIText.StringResource(R.string.disease_grape_healthy)
    "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)" -> UIText.StringResource(R.string.disease_grape_leaf_blight)
    "Orange___Haunglongbing_(Citrus_greening)" -> UIText.StringResource(R.string.disease_orange_haunglongbing)
    "Peach___Bacterial_spot" -> UIText.StringResource(R.string.disease_peach_bacterial_spot)
    "Peach___healthy" -> UIText.StringResource(R.string.disease_peach_healthy)
    "Pepper,_bell___Bacterial_spot" -> UIText.StringResource(R.string.disease_bell_paper_bacterial_spot)
    "Pepper,_bell___healthy" -> UIText.StringResource(R.string.disease_bell_pepper_healthy)
    "Potato___Early_blight" -> UIText.StringResource(R.string.disease_potato_early_blight)
    "Potato___healthy" -> UIText.StringResource(R.string.disease_potato_healthy)
    "Potato___Late_blight" -> UIText.StringResource(R.string.disease_potato_late_blight)
    "Raspberry___healthy" -> UIText.StringResource(R.string.disease_raspberry_healthy)
    "Soybean___healthy" -> UIText.StringResource(R.string.disease_soybean_healthy)
    "Squash___Powdery_mildew" -> UIText.StringResource(R.string.disease_squash_powdery_mildew)
    "Strawberry___healthy" -> UIText.StringResource(R.string.disease_strawberry_healthy)
    "Strawberry___Leaf_scorch" -> UIText.StringResource(R.string.disease_strawberry_leaf_scorch)
    "Tomato___Bacterial_spot" -> UIText.StringResource(R.string.disease_tomato_bacterial_spot)
    "Tomato___Early_blight" -> UIText.StringResource(R.string.disease_tomato_early_blight)
    "Tomato___healthy" -> UIText.StringResource(R.string.disease_tomato_healthy)
    "Tomato___Late_blight" -> UIText.StringResource(R.string.disease_tomato_late_blight)
    "Tomato___Leaf_Mold" -> UIText.StringResource(R.string.disease_tomato_leaf_mold)
    "Tomato___Septoria_leaf_spot" -> UIText.StringResource(R.string.disease_tomato_septoria_leaf_spot)
    "Tomato___Spider_mites Two-spotted_spider_mite" -> UIText.StringResource(R.string.disease_tomato_spider_mites)
    "Tomato___Target_Spot" -> UIText.StringResource(R.string.disease_tomato_target_spot)
    "Tomato___Tomato_mosaic_virus" -> UIText.StringResource(R.string.disease_tomato_tomato_mosaic_virus)
    "Tomato___Tomato_Yellow_Leaf_Curl_Virus" -> UIText.StringResource(R.string.disease_tomato_tomato_yellow_leaf_curl_virus)
    else -> UIText.DynamicString(label)
}