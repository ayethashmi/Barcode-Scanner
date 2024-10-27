package com.devappspros.barcodescanner.data.model.openLibraryResponse

import androidx.annotation.Keep
import com.devappspros.barcodescanner.data.model.openLibraryResponse.items.Item
import com.devappspros.barcodescanner.data.model.openLibraryResponse.records.Book
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class InformationSchema(
    @SerializedName("records")
    @Expose
    val records: Map<String, Book>? = null,

    @SerializedName("items")
    @Expose
    val items: List<Item>? = null) {

    fun getBook(): Book? = records?.values?.firstOrNull()
}