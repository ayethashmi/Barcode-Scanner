package com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse

import androidx.annotation.Keep
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.commons.DevAppsEnValue
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.commons.DevAppsLanguageValue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsCountryResponse (

    @SerializedName("country_code_2")
    @Expose
    val countryCode2: DevAppsEnValue? = null,

    @SerializedName("languages")
    @Expose
    val languages: DevAppsEnValue? = null,

    @SerializedName("country_code_3")
    @Expose
    val countryCode3: DevAppsEnValue? = null,

    @SerializedName("name")
    @Expose
    val name: DevAppsLanguageValue? = null
)