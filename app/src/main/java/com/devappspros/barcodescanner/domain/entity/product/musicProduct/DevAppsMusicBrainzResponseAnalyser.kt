package com.devappspros.barcodescanner.domain.entity.product.musicProduct

import com.devappspros.barcodescanner.data.model.musicBrainzResponse.musicAlbumInfo.DevAppsMusicAlbumInfoResponse
import com.devappspros.barcodescanner.data.model.musicBrainzResponse.musicAlbumInfo.DevAppsReleaseSchema

fun obtainId(response: DevAppsMusicAlbumInfoResponse): String? {
    val releases: List<DevAppsReleaseSchema>? = response.releases
    return if(!releases.isNullOrEmpty()) releases[0].id else null
}

fun obtainAlbum(response: DevAppsMusicAlbumInfoResponse): String? {
    val releases: List<DevAppsReleaseSchema>? = response.releases
    return if(!releases.isNullOrEmpty()) releases[0].album else null
}

fun obtainArtists(response: DevAppsMusicAlbumInfoResponse): List<String>? {
    val releases: List<DevAppsReleaseSchema>? = response.releases
    return if(!releases.isNullOrEmpty()) {
        releases[0].artists?.mapNotNull { it.name }
    } else null
}

fun obtainDate(response: DevAppsMusicAlbumInfoResponse): String? {
    val releases: List<DevAppsReleaseSchema>? = response.releases
    return if(!releases.isNullOrEmpty()) releases[0].date else null
}

fun obtainTrackCount(response: DevAppsMusicAlbumInfoResponse): Int? {
    val releases: List<DevAppsReleaseSchema>? = response.releases
    return if(!releases.isNullOrEmpty()) releases[0].trackCount else null
}