package com.devappspros.barcodescanner.domain.library.camera

import androidx.camera.core.ImageProxy
import com.devappspros.barcodescanner.common.extensions.toByteArray
import com.devappspros.barcodescanner.presentation.customView.DevAppsScanOverlay
import kotlin.math.roundToInt

class DevAppsCameraXBarcodeAnalyzerDevApps(
    barcodeDetector: BarcodeDetector
) : DevAppsAbstractCameraXBarcodeAnalyzer(barcodeDetector) {

    override fun analyze(image: ImageProxy) {
        val plane = image.planes[0]
        val imageData = plane.buffer.toByteArray()

        val size = image.width.coerceAtMost(image.height) * DevAppsScanOverlay.RATIO

        val left = (image.width - size) / 2f
        val top = (image.height - size) / 2f

        analyse(
            yuvData = imageData,
            dataWidth = plane.rowStride,
            dataHeight = image.height,
            left = left.roundToInt(),
            top = top.roundToInt(),
            width = size.roundToInt(),
            height = size.roundToInt()
        )

        image.close()
    }
}