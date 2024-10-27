package com.devappspros.barcodescanner.data.repositories

import androidx.lifecycle.LiveData
import com.devappspros.barcodescanner.data.database.CustomUrlDao
import com.devappspros.barcodescanner.domain.entity.customUrl.CustomUrl
import com.devappspros.barcodescanner.domain.repositories.DevAppsCustomUrlRepository

class DevAppsCustomUrlRepositoryImpl(private val customUrlDao: CustomUrlDao): DevAppsCustomUrlRepository {

    override fun getCustomUrlList(): LiveData<List<CustomUrl>> = customUrlDao.getCustomUrlList()

    override suspend fun insertCustomUrl(customUrl: CustomUrl): Long = customUrlDao.insert(customUrl)

    override suspend fun insertCustomUrls(customUrls: List<CustomUrl>) = customUrlDao.insert(customUrls)

    override suspend fun updateCustomUrl(customUrl: CustomUrl): Int = customUrlDao.update(customUrl.id, customUrl.name, customUrl.url)

    override suspend fun deleteAllCustomUrl(): Int = customUrlDao.deleteAll()

    override suspend fun deleteCustomUrls(customUrls: List<CustomUrl>): Int = customUrlDao.deleteCustomUrls(customUrls)

    override suspend fun deleteCustomUrl(customUrl: CustomUrl): Int = customUrlDao.delete(customUrl)
}