package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.api.OpenPetFoodFactsService
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.repositories.DevAppsPetFoodProductRepository

class DevAppsPetFoodProductRepositoryImpl(private val service: OpenPetFoodFactsService): DevAppsPetFoodProductRepository {
    override suspend fun getPetFoodProduct(barcode: Barcode): DevAppsFoodDevAppsBarcodeAnalysis? {
        val petFoodProductResponse =  service.getOpenPetFoodFactsData(barcode.contents)

        if(petFoodProductResponse.status == 0 || petFoodProductResponse.productResponse == null)
            return null

        return petFoodProductResponse.toModel(barcode, DevAppsRemoteAPI.OPEN_PET_FOOD_FACTS)
    }
}