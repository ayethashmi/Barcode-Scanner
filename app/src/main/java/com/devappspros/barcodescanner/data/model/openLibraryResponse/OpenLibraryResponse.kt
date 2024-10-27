package com.devappspros.barcodescanner.data.model.openLibraryResponse

import androidx.annotation.Keep
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBookDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainAuthorsStringList
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainCategories
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainContributions
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainCoverUrl
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainDescription
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainNumberPages
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainOriginalTitle
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainPublishDate
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainPublishers
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainSubtitle
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainTitle
import com.devappspros.barcodescanner.domain.entity.product.bookProduct.obtainUrl
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class OpenLibraryResponse(
    @SerializedName("information")
    @Expose
    val informationSchema: InformationSchema? = null
) {

    fun toModel(barcode: Barcode, source: DevAppsRemoteAPI): DevAppsBookDevAppsBarcodeAnalysis = DevAppsBookDevAppsBarcodeAnalysis(
        barcode =  barcode,
        source = source,
        url = obtainUrl(this),
        title = obtainTitle(this),
        subtitle = obtainSubtitle(this),
        originalTitle = obtainOriginalTitle(this),
        authors = obtainAuthorsStringList(this),
        coverUrl = obtainCoverUrl(this),
        description = obtainDescription(this),
        publishDate = obtainPublishDate(this),
        numberPages = obtainNumberPages(this),
        contributions = obtainContributions(this),
        publishers = obtainPublishers(this),
        categories = obtainCategories(this)
    )
}