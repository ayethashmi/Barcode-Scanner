package com.devappspros.barcodescanner.data.model.openLibraryResponse.records.data

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Name(
    @SerializedName("name")
    @Expose
    val name: String? = null
)