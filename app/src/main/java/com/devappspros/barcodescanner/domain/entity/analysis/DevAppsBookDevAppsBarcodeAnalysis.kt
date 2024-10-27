package com.devappspros.barcodescanner.domain.entity.analysis

import androidx.annotation.Keep
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode

@Keep
class DevAppsBookDevAppsBarcodeAnalysis(
    barcode: Barcode,
    source: DevAppsRemoteAPI,
    val url: String?,
    val title: String?,
    val subtitle: String?,
    val originalTitle: String?,
    val authors: List<String>?,
    val coverUrl: String?,
    val description: String?,
    val publishDate: String?,
    val numberPages: Int?,
    val contributions: List<String>?,
    val publishers: List<String>?,
    val categories: List<String>?
): DevAppsBarcodeAnalysis(barcode, source)