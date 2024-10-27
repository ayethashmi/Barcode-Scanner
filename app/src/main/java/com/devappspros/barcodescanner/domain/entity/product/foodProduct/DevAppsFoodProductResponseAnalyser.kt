package com.devappspros.barcodescanner.domain.entity.product.foodProduct

import com.devappspros.barcodescanner.common.utils.*
import com.devappspros.barcodescanner.data.model.openFoodFactsResponse.DevAppsFoodProductResponse
import com.devappspros.barcodescanner.data.model.openFoodFactsResponse.DevAppsIngredientResponse
import com.devappspros.barcodescanner.data.model.openFoodFactsResponse.DevAppsNutrimentsResponse

fun getNutriscore(productResponse: DevAppsFoodProductResponse?): DevAppsNutriscore {
    return when(productResponse?.nutritionGrades){
        "a" -> DevAppsNutriscore.A
        "b" -> DevAppsNutriscore.B
        "c" -> DevAppsNutriscore.C
        "d" -> DevAppsNutriscore.D
        "e" -> DevAppsNutriscore.E
        else  -> DevAppsNutriscore.UNKNOWN
    }
}

fun getEcoScore(productResponse: DevAppsFoodProductResponse?): DevAppsEcoScore {
    return when(productResponse?.ecoScoreGrade){
        "a" -> DevAppsEcoScore.A
        "b" -> DevAppsEcoScore.B
        "c" -> DevAppsEcoScore.C
        "d" -> DevAppsEcoScore.D
        "e" -> DevAppsEcoScore.E
        else  -> DevAppsEcoScore.UNKNOWN
    }
}

fun getNovaGroup(productResponse: DevAppsFoodProductResponse?): DevAppsNovaGroup {
    return when(productResponse?.novaGroup) {
        1 -> DevAppsNovaGroup.GROUP_1DevApps
        2 -> DevAppsNovaGroup.GROUP_2DevApps
        3 -> DevAppsNovaGroup.GROUP_3DevApps
        4 -> DevAppsNovaGroup.GROUP_4DevApps
        else -> DevAppsNovaGroup.UNKNOWN
    }
}

fun getVeggieIngredientAnalysisList(productResponse: DevAppsFoodProductResponse?): List<DevAppsVeggieIngredientAnalysis>? {

    val ingredientsList: List<DevAppsIngredientResponse>? = productResponse?.ingredientsResponses

    if (ingredientsList.isNullOrEmpty()) return null

    val devAppsVeggieIngredientAnalysisList = mutableListOf<DevAppsVeggieIngredientAnalysis>()

    for(ingredient in ingredientsList){

        devAppsVeggieIngredientAnalysisList.add(
            DevAppsVeggieIngredientAnalysis(
                ingredient.text,
                determineVeganStatus(ingredient.vegan),
                determineVegetarianStatus(ingredient.vegetarian)
            )
        )
    }
    return devAppsVeggieIngredientAnalysisList
}

private fun determineVeganStatus(veganTxt: String?): DevAppsVeganStatus {
    return when(veganTxt){
        "yes" -> DevAppsVeganStatus.DevAppsVEGAN
        "no" -> DevAppsVeganStatus.NO_DevApps_VEGAN
        "maybe" -> DevAppsVeganStatus.MAYBE_DevApps_VEGAN
        else -> DevAppsVeganStatus.UNKNOWN_DevApps_VEGAN
    }
}

private fun determineVegetarianStatus(veganTxt: String?): DevAppsVegetarianStatus {
    return when(veganTxt){
        "yes" -> DevAppsVegetarianStatus.DevAppsVEGETARIAN
        "no" -> DevAppsVegetarianStatus.NO_DevApps_VEGETARIAN
        "maybe" -> DevAppsVegetarianStatus.MAYBE_DevApps_VEGETARIAN
        else -> DevAppsVegetarianStatus.UNKNOWN_DevApps_VEGETARIAN
    }
}

fun getPalmOilStatus(productResponse: DevAppsFoodProductResponse?): DevAppsPalmOilStatus {

    var status: DevAppsPalmOilStatus = DevAppsPalmOilStatus.UNKNOWN_PALM_OIL
    productResponse?.ingredientsAnalysisTags?.forEach {
        when(it){
            "en:palm-oil-free" -> status = DevAppsPalmOilStatus.PALM_OIL_FREE
            "en:palm-oil" -> status = DevAppsPalmOilStatus.PALM_OIL
            "en:may-contain-palm-oil" -> status = DevAppsPalmOilStatus.MAYBE_PALM_OIL
            "en:palm-oil-content-unknown" -> status = DevAppsPalmOilStatus.UNKNOWN_PALM_OIL
        }
    }

    return status
}

fun getVeganStatus(productResponse: DevAppsFoodProductResponse?): DevAppsVeganStatus {

    var status: DevAppsVeganStatus = DevAppsVeganStatus.UNKNOWN_DevApps_VEGAN
    productResponse?.ingredientsAnalysisTags?.forEach {
        when(it){
            "en:vegan" -> status = DevAppsVeganStatus.DevAppsVEGAN
            "en:non-vegan" -> status = DevAppsVeganStatus.NO_DevApps_VEGAN
            "en:maybe-vegan" -> status = DevAppsVeganStatus.MAYBE_DevApps_VEGAN
            "en:vegan-status-unknown" -> status = DevAppsVeganStatus.UNKNOWN_DevApps_VEGAN
        }
    }

    return status
}

fun getVegetarianStatus(productResponse: DevAppsFoodProductResponse?): DevAppsVegetarianStatus {

    var status: DevAppsVegetarianStatus = DevAppsVegetarianStatus.UNKNOWN_DevApps_VEGETARIAN
    productResponse?.ingredientsAnalysisTags?.forEach {
        when(it){
            "en:vegetarian" -> status = DevAppsVegetarianStatus.DevAppsVEGETARIAN
            "en:non-vegetarian" -> status = DevAppsVegetarianStatus.NO_DevApps_VEGETARIAN
            "en:maybe-vegetarian" -> status = DevAppsVegetarianStatus.MAYBE_DevApps_VEGETARIAN
            "en:vegetarian-status-unknown" -> status = DevAppsVegetarianStatus.UNKNOWN_DevApps_VEGETARIAN
        }
    }

    return status
}

fun createNutrientsList(productResponse: DevAppsFoodProductResponse?): List<DevAppsNutrient>{
    val nutrimentsResponse = productResponse?.devAppsNutrimentsResponse
    val isBeverage = productResponse?.nutritionScoreBeverage==1

    val energyKJ = createEnergyKJNutrient(nutrimentsResponse)
    val energyKcal = createEnergyKcalNutrient(nutrimentsResponse)
    val fat = createFatNutrient(nutrimentsResponse, isBeverage)
    val saturatedFat = createSaturatedFatNutrient(nutrimentsResponse, isBeverage)
    val carbohydrates = createCarbohydratesNutrient(nutrimentsResponse)
    val sugars = createSugarsNutrient(nutrimentsResponse, isBeverage)
    val starch = createStarchNutrient(nutrimentsResponse)
    val fiber = createFiberNutrient(nutrimentsResponse)
    val proteins = createProteinsNutrient(nutrimentsResponse)
    val salt = createSaltNutrient(nutrimentsResponse, isBeverage)
    val sodium = createSodiumNutrient(nutrimentsResponse)

    return listOfNotNull(energyKJ, energyKcal, fat, saturatedFat, carbohydrates, sugars, starch, fiber, proteins, salt, sodium)
}

private fun createEnergyKJNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.ENERGY_KJ,
    value100g = devAppsNutrimentsResponse?.energyKj100g,
    valueServing = devAppsNutrimentsResponse?.energyKjServing,
    unit = devAppsNutrimentsResponse?.energyKjUnit
)

private fun createEnergyKcalNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.ENERGY_KCAL,
    value100g = devAppsNutrimentsResponse?.energyKcal100g,
    valueServing = devAppsNutrimentsResponse?.energyKcalServing,
    unit = devAppsNutrimentsResponse?.energyKcalUnit
)

private fun createFatNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?, isBeverage: Boolean): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.FAT,
    value100g = devAppsNutrimentsResponse?.fat100g,
    valueServing = devAppsNutrimentsResponse?.fatServing,
    unit = devAppsNutrimentsResponse?.fatUnit,
    valueLow = FAT_VALUE_LOW,
    valueHigh = FAT_VALUE_HIGH,
    isBeverage = isBeverage
)

private fun createSaturatedFatNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?, isBeverage: Boolean): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.SATURATED_FAT,
    value100g = devAppsNutrimentsResponse?.saturatedFat100g,
    valueServing = devAppsNutrimentsResponse?.saturatedFatServing,
    unit = devAppsNutrimentsResponse?.saturatedFatUnit,
    valueLow = SATURATED_FAT_VALUE_LOW,
    valueHigh = SATURATED_FAT_VALUE_HIGH,
    isBeverage = isBeverage
)

private fun createCarbohydratesNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.CARBOHYDRATES,
    value100g = devAppsNutrimentsResponse?.carbohydrates100g,
    valueServing = devAppsNutrimentsResponse?.carbohydratesServing,
    unit = devAppsNutrimentsResponse?.carbohydratesUnit
)

private fun createSugarsNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?, isBeverage: Boolean): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.SUGARS,
    value100g = devAppsNutrimentsResponse?.sugars100g,
    valueServing = devAppsNutrimentsResponse?.sugarsServing,
    unit = devAppsNutrimentsResponse?.sugarsUnit,
    valueLow = SUGAR_VALUE_LOW,
    valueHigh = SUGAR_VALUE_HIGH,
    isBeverage = isBeverage
)

private fun createStarchNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.STARCH,
    value100g = devAppsNutrimentsResponse?.starch100g,
    valueServing = devAppsNutrimentsResponse?.starchServing,
    unit = devAppsNutrimentsResponse?.starchUnit
)

private fun createFiberNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.FIBER,
    value100g = devAppsNutrimentsResponse?.fiber100g,
    valueServing = devAppsNutrimentsResponse?.fiberServing,
    unit = devAppsNutrimentsResponse?.fiberUnit
)

private fun createProteinsNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.PROTEINS,
    value100g = devAppsNutrimentsResponse?.proteins100g,
    valueServing = devAppsNutrimentsResponse?.proteinsServing,
    unit = devAppsNutrimentsResponse?.proteinsUnit
)

private fun createSaltNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?, isBeverage: Boolean): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.SALT,
    value100g = devAppsNutrimentsResponse?.salt100g,
    valueServing = devAppsNutrimentsResponse?.saltServing,
    unit = devAppsNutrimentsResponse?.saltUnit,
    valueLow = SALT_VALUE_LOW,
    valueHigh = SALT_VALUE_HIGH,
    isBeverage = isBeverage
)

private fun createSodiumNutrient(devAppsNutrimentsResponse: DevAppsNutrimentsResponse?): DevAppsNutrient? = createNutrient(
    devAppsNutritionFactsEnum = DevAppsNutritionFactsEnum.SODIUM,
    value100g = devAppsNutrimentsResponse?.sodium100g,
    valueServing = devAppsNutrimentsResponse?.sodiumServing,
    unit = devAppsNutrimentsResponse?.sodiumUnit
)

private fun createNutrient(devAppsNutritionFactsEnum: DevAppsNutritionFactsEnum,
                           value100g: Number?,
                           valueServing: Number?,
                           unit: String?,
                           valueLow: Float? = null,
                           valueHigh: Float? = null,
                           isBeverage: Boolean? = null): DevAppsNutrient? {

    // Dans NutrientsResponse, on récupère les valeurs Number au format com.google.gson.internal.LazilyParsedNumber.
    // Si il n y a pas de valeur on obtient une valeur vide et non null. Si c'est le cas on fait en sorte de récupérer une valeur null.
    val newValue100g = if(value100g.toString().isBlank()) null else value100g
    val newValueServing = if(valueServing.toString().isBlank()) null else valueServing

    if(newValue100g == null && newValueServing == null) return null

    val values = DevAppsNutrient.NutrientValues(newValue100g, newValueServing, unit ?: "")
    val quantity = createQuantity(valueLow, valueHigh, isBeverage)
    return DevAppsNutrient(devAppsNutritionFactsEnum, values, quantity)
}

private fun createQuantity(valueLow: Float? = null,
                           valueHigh: Float? = null,
                           isBeverage: Boolean? = null): DevAppsNutrient.Quantity? {
    if(valueLow == null || valueHigh == null || isBeverage == null) return null

    return DevAppsNutrient.Quantity(valueLow, valueHigh, isBeverage)
}