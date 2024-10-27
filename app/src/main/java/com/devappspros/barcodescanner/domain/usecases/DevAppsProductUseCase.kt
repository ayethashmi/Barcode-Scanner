/*
 * Barcode Scanner
 * Copyright (C) 2021  Atharok
 *
 * This file is part of Barcode Scanner.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.devappspros.barcodescanner.domain.usecases

import androidx.lifecycle.MutableLiveData
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPIError
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsUnknownProductDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeType
import com.devappspros.barcodescanner.domain.repositories.DevAppsBeautyProductRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsBookProductRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsFoodProductRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsMusicProductRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsPetFoodProductRepository
import com.devappspros.barcodescanner.domain.resources.Resource

class DevAppsProductUseCase(private val devAppsFoodProductRepository: DevAppsFoodProductRepository,
                            private val devAppsBeautyProductRepository: DevAppsBeautyProductRepository,
                            private val devAppsPetFoodProductRepository: DevAppsPetFoodProductRepository,
                            private val devAppsMusicProductRepository: DevAppsMusicProductRepository,
                            private val devAppsBookProductRepository: DevAppsBookProductRepository) {

    val productObserver = MutableLiveData<Resource<DevAppsBarcodeAnalysis>>()

    suspend fun fetchProduct(barcode: Barcode, devAppsRemoteAPI: DevAppsRemoteAPI) {
        productObserver.postValue(Resource.loading())

        try {
            val devAppsBarcodeAnalysis: DevAppsBarcodeAnalysis = when(devAppsRemoteAPI) {
                DevAppsRemoteAPI.OPEN_FOOD_FACTS -> devAppsFoodProductRepository.getFoodProduct(barcode) ?: DevAppsUnknownProductDevAppsBarcodeAnalysis(barcode, DevAppsRemoteAPIError.NO_RESULT, source = devAppsRemoteAPI)
                DevAppsRemoteAPI.OPEN_BEAUTY_FACTS -> devAppsBeautyProductRepository.getBeautyProduct(barcode) ?: DevAppsUnknownProductDevAppsBarcodeAnalysis(barcode, DevAppsRemoteAPIError.NO_RESULT, source = devAppsRemoteAPI)
                DevAppsRemoteAPI.OPEN_PET_FOOD_FACTS -> devAppsPetFoodProductRepository.getPetFoodProduct(barcode) ?: DevAppsUnknownProductDevAppsBarcodeAnalysis(barcode, DevAppsRemoteAPIError.NO_RESULT, source = devAppsRemoteAPI)
                DevAppsRemoteAPI.MUSICBRAINZ -> devAppsMusicProductRepository.getMusicProduct(barcode) ?: DevAppsUnknownProductDevAppsBarcodeAnalysis(barcode, DevAppsRemoteAPIError.NO_RESULT, source = devAppsRemoteAPI)
                DevAppsRemoteAPI.OPEN_LIBRARY -> devAppsBookProductRepository.getBookProduct(barcode) ?: DevAppsUnknownProductDevAppsBarcodeAnalysis(barcode, DevAppsRemoteAPIError.NO_RESULT, source = devAppsRemoteAPI)
                DevAppsRemoteAPI.NONE -> DevAppsUnknownProductDevAppsBarcodeAnalysis(
                    barcode = barcode,
                    apiError = DevAppsRemoteAPIError.AUTOMATIC_API_RESEARCH_DISABLED,
                    source = when(barcode.getBarcodeType()) {
                        BarcodeType.FOOD -> DevAppsRemoteAPI.OPEN_FOOD_FACTS
                        BarcodeType.PET_FOOD -> DevAppsRemoteAPI.OPEN_PET_FOOD_FACTS
                        BarcodeType.BEAUTY -> DevAppsRemoteAPI.OPEN_BEAUTY_FACTS
                        BarcodeType.MUSIC -> DevAppsRemoteAPI.MUSICBRAINZ
                        BarcodeType.BOOK -> DevAppsRemoteAPI.OPEN_LIBRARY
                        else -> devAppsRemoteAPI
                    }
                )
            }

            productObserver.postValue(Resource.success(devAppsBarcodeAnalysis))

        } catch (e: Exception) {
            productObserver.postValue(Resource.failure(e, DevAppsUnknownProductDevAppsBarcodeAnalysis(barcode, DevAppsRemoteAPIError.ERROR, e.toString(), devAppsRemoteAPI)))
            e.printStackTrace()
        }
    }
}