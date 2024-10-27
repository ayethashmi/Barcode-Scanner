package com.devappspros.barcodescanner.data.model.covertArtArchiveResponse

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsImageSchema(
    @SerializedName("image")
    @Expose
    val imageUrl: String? = null,

    @SerializedName("thumbnails")
    @Expose
    val devAppsThumbnails: DevAppsThumbnails? = null
)