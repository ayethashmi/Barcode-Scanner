package com.devappspros.barcodescanner.data.model.musicBrainzResponse.musicAlbumInfo

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsArtistSchema(
    @SerializedName("name")
    @Expose
    val name: String? = null
)