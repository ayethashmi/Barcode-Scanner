package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.api.OpenLibraryService
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBookDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.repositories.DevAppsBookProductRepository

class DevAppsBookProductRepositoryImpl(private val service: OpenLibraryService): DevAppsBookProductRepository {
    override suspend fun getBookProduct(barcode: Barcode): DevAppsBookDevAppsBarcodeAnalysis? {
        val bookProductResponse = service.getOpenLibraryData(barcode.contents)

        if(bookProductResponse.informationSchema == null)
            return null

        return bookProductResponse.toModel(barcode, DevAppsRemoteAPI.OPEN_LIBRARY)
    }
}