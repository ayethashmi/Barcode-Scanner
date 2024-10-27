package com.devappspros.barcodescanner.domain.entity.analysis

import androidx.annotation.Keep
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsEcoScore
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNovaGroup
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNutrient
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNutriscore
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNutritionFactsEnum
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsPalmOilStatus
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsVeganStatus
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsVegetarianStatus
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsVeggieIngredientAnalysis

@Keep
class DevAppsFoodDevAppsBarcodeAnalysis(
    barcode: Barcode,
    source: DevAppsRemoteAPI,
    val name: String?,
    val brands: String?,
    val quantity: String?,
    val imageFrontUrl: String?,
    val labels: String?,
    val labelsTagList: List<String>?,
    val categories: String?,
    val packaging: String?,
    val stores: String?,
    val salesCountriesTagsList: List<String>?,
    val originsCountriesTagsList: List<String>?,
    val devAppsNutriscore: DevAppsNutriscore,
    val devAppsNovaGroup: DevAppsNovaGroup,
    val devAppsEcoScore: DevAppsEcoScore,
    val ingredients: String?,
    val tracesTagsList: List<String>?,
    val allergensTagsList: List<String>?,
    val additivesTagsList: List<String>?,
    val veggieIngredientList: List<DevAppsVeggieIngredientAnalysis>?,
    val devAppsVeganStatus: DevAppsVeganStatus,
    val devAppsVegetarianStatus: DevAppsVegetarianStatus,
    val devAppsPalmOilStatus: DevAppsPalmOilStatus,
    val servingQuantity: Double?,
    val unit: String,
    val nutrientsList: List<DevAppsNutrient>
): DevAppsBarcodeAnalysis(barcode, source) {

    val contains100gValues: Boolean = nutrientsList.any { it.values.value100g != null }

    val containsServingValues: Boolean = nutrientsList.any { it.values.valueServing != null }

    /**
     * Vérifie si on possède un Nutrient de type FAT ou SATURATED_FAT ou SUGARS ou SALT
     */
    val containsNutrientLevel: Boolean = nutrientsList.any {
        it.entitled == DevAppsNutritionFactsEnum.FAT ||
        it.entitled == DevAppsNutritionFactsEnum.SATURATED_FAT ||
        it.entitled == DevAppsNutritionFactsEnum.SUGARS ||
        it.entitled == DevAppsNutritionFactsEnum.SALT
    }

    // On met les tags des allergens et des traces ensemble car les 2 sont trouvables dans le même fichier (allergens.json)
    val allergensAndTracesTagList: List<String>? = when {
        allergensTagsList != null && tracesTagsList != null -> allergensTagsList + tracesTagsList
        allergensTagsList != null -> allergensTagsList
        tracesTagsList != null -> tracesTagsList
        else -> null
    }?.distinct()?.toList()

    val countriesTagList: List<String>? = when {
        salesCountriesTagsList != null && originsCountriesTagsList != null -> salesCountriesTagsList + originsCountriesTagsList
        salesCountriesTagsList != null -> salesCountriesTagsList
        originsCountriesTagsList != null -> originsCountriesTagsList
        else -> null
    }?.distinct()?.toList()
}