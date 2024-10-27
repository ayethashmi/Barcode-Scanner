package com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.commons

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsEnValue(
    @SerializedName("en")
    @Expose
    val value: String? = null
)