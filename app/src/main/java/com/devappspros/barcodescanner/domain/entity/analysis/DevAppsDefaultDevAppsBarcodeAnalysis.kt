package com.devappspros.barcodescanner.domain.entity.analysis

import androidx.annotation.Keep
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode

@Keep
class DevAppsDefaultDevAppsBarcodeAnalysis(
    barcode: Barcode,
    source: DevAppsRemoteAPI = DevAppsRemoteAPI.NONE
): DevAppsBarcodeAnalysis(barcode, source)