package com.devappspros.barcodescanner.data.model.musicBrainzResponse.musicAlbumInfo

import androidx.annotation.Keep
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsMusicDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.DevAppsAlbumTrack
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.obtainAlbum
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.obtainArtists
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.obtainDate
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.obtainId
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.obtainTrackCount
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsMusicAlbumInfoResponse(
    @SerializedName("count")
    @Expose
    val count: Int? = null,

    @SerializedName("releases")
    @Expose
    val releases: List<DevAppsReleaseSchema>? = null
) {
    fun toModel(barcode: Barcode, source: DevAppsRemoteAPI, coverUrl: String?, devAppsAlbumTracks: List<DevAppsAlbumTrack>?): DevAppsMusicDevAppsBarcodeAnalysis = DevAppsMusicDevAppsBarcodeAnalysis(
        barcode = barcode,
        source = source,
        id = obtainId(this),
        artists = obtainArtists(this),
        album = obtainAlbum(this),
        date = obtainDate(this),
        trackCount = obtainTrackCount(this),
        coverUrl = coverUrl,
        devAppsAlbumTracks = devAppsAlbumTracks
    )
}