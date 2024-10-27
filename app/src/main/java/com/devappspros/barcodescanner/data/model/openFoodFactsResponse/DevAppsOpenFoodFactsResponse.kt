package com.devappspros.barcodescanner.data.model.openFoodFactsResponse

import androidx.annotation.Keep
import com.devappspros.barcodescanner.common.extensions.polishText
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.createNutrientsList
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.getEcoScore
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.getNovaGroup
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.getNutriscore
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.getPalmOilStatus
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.getVeganStatus
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.getVegetarianStatus
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.getVeggieIngredientAnalysisList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsOpenFoodFactsResponse(
    @SerializedName("status")
    @Expose
    var status: Int = 0,

    @SerializedName("code")
    @Expose
    var code: String? = null,

    @SerializedName("product")
    @Expose
    var productResponse: DevAppsFoodProductResponse? = null
) {

    fun toModel(barcode: Barcode, source: DevAppsRemoteAPI): DevAppsFoodDevAppsBarcodeAnalysis = DevAppsFoodDevAppsBarcodeAnalysis(
        barcode = barcode,
        source = source,
        name = productResponse?.productName?.polishText(),
        brands = productResponse?.brands?.polishText()?.polishText(),
        quantity = productResponse?.quantity?.polishText(),
        imageFrontUrl = productResponse?.imageFrontUrl,
        categories = productResponse?.categories?.polishText(),
        labels = productResponse?.labels?.polishText(),
        labelsTagList = productResponse?.labelsTags,
        packaging = productResponse?.packaging?.polishText(),
        stores = productResponse?.stores?.polishText(),
        salesCountriesTagsList = productResponse?.countriesTags,
        originsCountriesTagsList = productResponse?.originsTags,
        devAppsNutriscore = getNutriscore(productResponse),
        devAppsNovaGroup = getNovaGroup(productResponse),
        devAppsEcoScore = getEcoScore(productResponse),
        ingredients = productResponse?.ingredientsTextWithAllergens ?: productResponse?.ingredientsText ?: productResponse?.ingredientsTextWithAllergensFr ?: productResponse?.ingredientsTextFr,
        tracesTagsList = productResponse?.tracesTags,
        allergensTagsList = productResponse?.allergensTags,
        additivesTagsList = productResponse?.additivesTags,
        veggieIngredientList = getVeggieIngredientAnalysisList(productResponse),
        devAppsVeganStatus = getVeganStatus(productResponse),
        devAppsVegetarianStatus = getVegetarianStatus(productResponse),
        devAppsPalmOilStatus = getPalmOilStatus(productResponse),
        servingQuantity = productResponse?.servingQuantity,
        unit = if (productResponse?.nutritionScoreBeverage == 1) "ml" else "g",
        nutrientsList = createNutrientsList(productResponse)
    )
}