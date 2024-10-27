package com.devappspros.barcodescanner.domain.entity.analysis

import androidx.annotation.Keep
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import java.io.Serializable

@Keep
open class DevAppsBarcodeAnalysis(
    val barcode: Barcode,
    val source: DevAppsRemoteAPI
): Serializable