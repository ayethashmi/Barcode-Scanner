package com.devappspros.barcodescanner.domain.entity.analysis

import androidx.annotation.Keep
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.DevAppsAlbumTrack

@Keep
class DevAppsMusicDevAppsBarcodeAnalysis(
    barcode: Barcode,
    source: DevAppsRemoteAPI,
    val id: String?,
    val artists: List<String>?,
    val album: String?,
    val date: String?,
    val trackCount: Int?,
    val coverUrl: String?,
    val devAppsAlbumTracks: List<DevAppsAlbumTrack>?
): DevAppsBarcodeAnalysis(barcode, source)