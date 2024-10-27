package com.devappspros.barcodescanner.domain.entity.product.musicProduct

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class DevAppsAlbumTrack(val title: String?,
                             val length: Long?,
                             val position: Int?): Serializable