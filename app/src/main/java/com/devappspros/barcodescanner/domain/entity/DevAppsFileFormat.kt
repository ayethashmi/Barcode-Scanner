package com.devappspros.barcodescanner.domain.entity

enum class DevAppsFileFormat(val mimeType: String, val extension: String) {
    CSV("text/csv", ".csv"),
    JSON("application/json", ".json")
}