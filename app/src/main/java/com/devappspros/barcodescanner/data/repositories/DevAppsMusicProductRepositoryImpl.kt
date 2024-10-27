package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.api.CoverArtArchiveService
import com.devappspros.barcodescanner.data.api.MusicBrainzService
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsMusicDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.DevAppsAlbumTrack
import com.devappspros.barcodescanner.domain.repositories.DevAppsMusicProductRepository

class DevAppsMusicProductRepositoryImpl(
    private val musicBrainzService: MusicBrainzService,
    private val coverArtService: CoverArtArchiveService
): DevAppsMusicProductRepository {
    override suspend fun getMusicProduct(barcode: Barcode): DevAppsMusicDevAppsBarcodeAnalysis? {
        val musicAlbumInfoResponse = musicBrainzService.getAlbumInfoFromBarcode("barcode:${barcode.contents}")

        if(musicAlbumInfoResponse?.releases == null || musicAlbumInfoResponse.count == 0)
            return null

        val id: String? = musicAlbumInfoResponse.releases.firstOrNull()?.id
        val coverUrl: String? = id?.let { getCoverArtUrl(it) }
        val devAppsAlbumTracks: List<DevAppsAlbumTrack>? = id?.let { getAlbumTracks(it) }

        return musicAlbumInfoResponse.toModel(barcode, DevAppsRemoteAPI.MUSICBRAINZ, coverUrl, devAppsAlbumTracks)
    }

    private suspend fun getCoverArtUrl(discId: String): String? {
        return try {
            val response = coverArtService.getCoverArt(discId)
            (response?.images?.firstOrNull()?.devAppsThumbnails?.small ?: response?.images?.firstOrNull()?.imageUrl)?.replace("http://", "https://")
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getAlbumTracks(discId: String): List<DevAppsAlbumTrack>? {
        return try {
            val response = musicBrainzService.getAlbumTracks(discId)
            response?.toModel()
        } catch (e: Exception) {
            null
        }
    }
}