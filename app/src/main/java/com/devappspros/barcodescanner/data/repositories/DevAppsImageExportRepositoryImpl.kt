package com.devappspros.barcodescanner.data.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.devappspros.barcodescanner.data.file.FileStream
import com.devappspros.barcodescanner.data.file.image.BitmapSharer
import com.devappspros.barcodescanner.domain.repositories.DevAppsImageExportRepository

class DevAppsImageExportRepositoryImpl(
    private val fileStream: FileStream,
    private val sharer: BitmapSharer
): DevAppsImageExportRepository {

    override fun exportToPng(bitmap: Bitmap, uri: Uri): Boolean {
        return fileStream.export(uri) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    override fun exportToJpg(bitmap: Bitmap, uri: Uri): Boolean {
        return fileStream.export(uri) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    override fun exportToSvg(svg: String, uri: Uri): Boolean {
        return fileStream.export(uri) {
            it.write(svg.toByteArray())
        }
    }

    override fun shareBitmap(bitmap: Bitmap): Uri? {
        return sharer.share(bitmap)
    }
}