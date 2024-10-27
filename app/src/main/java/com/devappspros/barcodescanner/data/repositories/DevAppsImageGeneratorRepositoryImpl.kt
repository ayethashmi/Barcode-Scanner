package com.devappspros.barcodescanner.data.repositories

import android.graphics.Bitmap
import com.devappspros.barcodescanner.data.file.image.BarcodeBitmapGenerator
import com.devappspros.barcodescanner.data.file.image.BarcodeSvgGenerator
import com.devappspros.barcodescanner.domain.library.DevAppsBarcodeImageGeneratorProperties
import com.devappspros.barcodescanner.domain.repositories.DevAppsImageGeneratorRepository

class DevAppsImageGeneratorRepositoryImpl(
    private val bitmapGenerator: BarcodeBitmapGenerator,
    private val svgGenerator: BarcodeSvgGenerator
): DevAppsImageGeneratorRepository {

    override fun createBitmap(properties: DevAppsBarcodeImageGeneratorProperties): Bitmap? {
        return bitmapGenerator.create(properties)
    }

    override fun createSvg(properties: DevAppsBarcodeImageGeneratorProperties): String? {
        return svgGenerator.create(properties)
    }
}