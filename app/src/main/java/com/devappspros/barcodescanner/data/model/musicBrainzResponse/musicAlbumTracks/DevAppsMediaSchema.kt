package com.devappspros.barcodescanner.data.model.musicBrainzResponse.musicAlbumTracks

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsMediaSchema(
    @SerializedName("tracks")
    @Expose
    val tracks: List<DevAppsAlbumTrackSchema>? = null
)