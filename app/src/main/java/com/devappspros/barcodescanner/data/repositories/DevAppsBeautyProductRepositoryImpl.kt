package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.api.OpenBeautyFactsService
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.repositories.DevAppsBeautyProductRepository

class DevAppsBeautyProductRepositoryImpl(private val service: OpenBeautyFactsService): DevAppsBeautyProductRepository {
    override suspend fun getBeautyProduct(barcode: Barcode): DevAppsFoodDevAppsBarcodeAnalysis? {
        val beautyProductResponse = service.getOpenBeautyFactsData(barcode.contents)

        if(beautyProductResponse.status == 0 || beautyProductResponse.productResponse == null)
            return null

        return beautyProductResponse.toModel(barcode, DevAppsRemoteAPI.OPEN_BEAUTY_FACTS)
    }
}