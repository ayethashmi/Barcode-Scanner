package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.api.OpenFoodFactsService
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.repositories.DevAppsFoodProductRepository

class DevAppsFoodProductRepositoryImpl(private val service: OpenFoodFactsService): DevAppsFoodProductRepository {
    override suspend fun getFoodProduct(barcode: Barcode): DevAppsFoodDevAppsBarcodeAnalysis? {
        val foodProductResponse = service.getOpenFoodFactsData(barcode.contents)

        if(foodProductResponse.status == 0 || foodProductResponse.productResponse == null)
            return null

        return foodProductResponse.toModel(barcode, DevAppsRemoteAPI.OPEN_FOOD_FACTS)
    }
}