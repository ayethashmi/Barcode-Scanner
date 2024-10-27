package com.devappspros.barcodescanner.domain.entity

enum class DevAppsImageFormat(val mimeType: String) {
    PNG("image/png"),
    JPG("image/jpeg"),
    SVG("image/svg+xml")
}