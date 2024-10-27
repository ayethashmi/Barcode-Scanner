package com.devappspros.barcodescanner.data.api

import com.devappspros.barcodescanner.data.model.openFoodFactsResponse.DevAppsOpenFoodFactsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenBeautyFactsService {

    @GET("{productCode}")
    suspend fun getOpenBeautyFactsData(@Path("productCode") productCode: String): DevAppsOpenFoodFactsResponse
}