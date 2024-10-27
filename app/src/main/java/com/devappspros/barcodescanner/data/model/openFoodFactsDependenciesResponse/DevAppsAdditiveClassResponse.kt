package com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse

import androidx.annotation.Keep
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.commons.DevAppsEnValue
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.commons.DevAppsLanguageValue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsAdditiveClassResponse(
    @SerializedName("name")
    @Expose
    val name: DevAppsLanguageValue? = null,

    @SerializedName("description")
    @Expose
    val description: DevAppsLanguageValue? = null,

    @SerializedName("wikidata")
    @Expose
    val wikidata: DevAppsEnValue? = null
)