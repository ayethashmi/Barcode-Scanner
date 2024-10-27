package com.devappspros.barcodescanner.domain.entity.analysis

import androidx.annotation.Keep
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode

@Keep
class DevAppsUnknownProductDevAppsBarcodeAnalysis(
    barcode: Barcode,
    val apiError: DevAppsRemoteAPIError,
    val message: String? = null,
    source: DevAppsRemoteAPI = DevAppsRemoteAPI.NONE
): DevAppsBarcodeAnalysis(barcode, source)