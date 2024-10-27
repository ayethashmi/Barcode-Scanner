package com.devappspros.barcodescanner.data.model.musicBrainzResponse.musicAlbumTracks

import androidx.annotation.Keep
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.DevAppsAlbumTrack
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsMusicAlbumTracksResponse(
    @SerializedName("count")
    @Expose
    val count: Int? = null,

    @SerializedName("media")
    @Expose
    val medias: List<DevAppsMediaSchema>? = null
) {
    fun toModel(): List<DevAppsAlbumTrack>? {
        return medias?.firstOrNull()?.tracks?.map {
            DevAppsAlbumTrack(it.title, it.length, it.position)
        }
    }
}