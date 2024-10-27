package com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse

import androidx.annotation.Keep
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.commons.DevAppsEnValue
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.commons.DevAppsLanguageValue
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Cette classe est une valeur de la liste du fichier "allergens.json" convertit en Kotlin. Le
 * fichier est récupéré via l'URL "https://world.openfoodfacts.org/data/taxonomies/allergens.json".
 */
@Keep
data class DevAppsAllergenResponse(
    @SerializedName("name")
    @Expose
    val name: DevAppsLanguageValue? = null,

    @SerializedName("wikidata")
    @Expose
    val wikidata: DevAppsEnValue? = null
)