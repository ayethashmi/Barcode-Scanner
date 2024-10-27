package com.devappspros.barcodescanner.domain.repositories

import com.devappspros.barcodescanner.domain.entity.dependencies.AdditiveClass

interface DevAppsAdditiveClassRepository {
    suspend fun getAdditiveClassList(fileNameWithExtension: String, fileUrlName: String, tagList: List<String>): List<AdditiveClass>
}