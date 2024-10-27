package com.devappspros.barcodescanner.data.model.covertArtArchiveResponse

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class DevAppsCoverArtArchiveResponse(
    @SerializedName("images")
    @Expose
    val images: List<DevAppsImageSchema>? = null
)