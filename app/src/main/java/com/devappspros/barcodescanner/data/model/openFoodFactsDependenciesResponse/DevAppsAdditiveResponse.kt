package com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse

import androidx.annotation.Keep
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.commons.DevAppsEnValue
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.commons.DevAppsLanguageValue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Cette classe est une valeur de la liste "additives.json" convertit en Kotlin. Le
 * fichier est récupéré via l'URL "https://world.openfoodfacts.org/data/taxonomies/additives.json".
 */
@Keep
data class DevAppsAdditiveResponse(
    @SerializedName("vegan")
    @Expose
    val vegan: DevAppsEnValue? = null,

    @SerializedName("vegetarian")
    @Expose
    val vegetarian: DevAppsEnValue? = null,

    @SerializedName("efsa_evaluation")
    @Expose
    val efsaEvaluation: DevAppsEnValue? = null,

    @SerializedName("additives_classes")
    @Expose
    val additivesClasses: DevAppsEnValue? = null,

    @SerializedName("efsa_evaluation_overexposure_risk")
    @Expose
    val efsaEvaluationOverexposureRisk: DevAppsEnValue? = null,

    @SerializedName("organic_eu")
    @Expose
    val organicEu: DevAppsEnValue? = null,

    @SerializedName("e_number")
    @Expose
    val eNumber: DevAppsEnValue? = null,

    @SerializedName("wikidata")
    @Expose
    val wikidata: DevAppsEnValue? = null,

    @SerializedName("efsa_evaluation_date")
    @Expose
    val efsaEvaluationDate: DevAppsEnValue? = null,

    @SerializedName("efsa_evaluation_url")
    @Expose
    val efsaEvaluationUrl: DevAppsEnValue? = null,

    @SerializedName("name")
    @Expose
    val name: DevAppsLanguageValue? = null,

    @SerializedName("efsa_evaluation_exposure_95th_greater_than_adi")
    @Expose
    val efsaEvaluationExposure95thGreaterThanAdi: DevAppsEnValue? = null,

    @SerializedName("efsa_evaluation_exposure_mean_greater_than_adi")
    @Expose
    val efsaEvaluationExposureMeanGreaterThanAdi: DevAppsEnValue? = null
) {
    var tag: String = ""
}