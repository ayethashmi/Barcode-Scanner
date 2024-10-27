package com.devappspros.barcodescanner.domain.library

import android.graphics.Color
import androidx.annotation.ColorInt
import com.devappspros.barcodescanner.common.extensions.is2DBarcode
import com.devappspros.barcodescanner.common.utils.BARCODE_IMAGE_DEFAULT_SIZE
import com.devappspros.barcodescanner.common.utils.ENCODING_ISO_8859_1
import com.devappspros.barcodescanner.common.utils.ENCODING_UTF_8
import com.devappspros.barcodescanner.domain.entity.barcode.QrCodeErrorCorrectionLevel
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import java.io.Serializable

data class DevAppsBarcodeImageGeneratorProperties(
    val contents: String,
    val format: BarcodeFormat,
    val qrCodeErrorCorrectionLevel: QrCodeErrorCorrectionLevel? = null,
    private val size: Int = BARCODE_IMAGE_DEFAULT_SIZE,
    @ColorInt var frontColor: Int = Color.BLACK,
    @ColorInt var backgroundColor: Int = Color.WHITE
): Serializable {

    val is2DBarcode: Boolean = format.is2DBarcode()

    var width: Int = size
    var height: Int = if(is2DBarcode && format != BarcodeFormat.PDF_417) size else size / 2
    val widthF: Float get() = width.toFloat()
    val heightF: Float get() = height.toFloat()

    val contentsHeight: Float get() = if(is2DBarcode) 0f else (width / (contents.length + 2f)) // ajuster la taille du texte en fonction de la largeur de l'image et du contenu

    var cornerRadius: Float = 0.0f
        set(value) {
            field = if(value < 0f) 0f else if(value>1f) 1f else value
        }

    val hints: Map<EncodeHintType, Any>
        get() {
            val encoding: String = when(format) {
                BarcodeFormat.QR_CODE, BarcodeFormat.PDF_417 -> ENCODING_UTF_8
                else -> ENCODING_ISO_8859_1
            }

            return qrCodeErrorCorrectionLevel?.errorCorrectionLevel?.let {
                mapOf<EncodeHintType, Any>(EncodeHintType.CHARACTER_SET to encoding, EncodeHintType.ERROR_CORRECTION to it)
            } ?: run {
                mapOf<EncodeHintType, Any>(EncodeHintType.CHARACTER_SET to encoding)
            }
        }
}