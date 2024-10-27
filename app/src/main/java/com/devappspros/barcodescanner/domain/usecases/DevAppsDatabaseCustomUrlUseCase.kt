/*
 * Barcode Scanner
 * Copyright (C) 2021  Atharok
 *
 * This file is part of Barcode Scanner.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.devappspros.barcodescanner.domain.usecases

import androidx.lifecycle.LiveData
import com.devappspros.barcodescanner.domain.entity.customUrl.CustomUrl
import com.devappspros.barcodescanner.domain.repositories.DevAppsCustomUrlRepository

class DevAppsDatabaseCustomUrlUseCase(private val devAppsCustomUrlRepository: DevAppsCustomUrlRepository) {

    val customUrlList: LiveData<List<CustomUrl>> = devAppsCustomUrlRepository.getCustomUrlList()

    suspend fun insertCustomUrl(customUrl: CustomUrl): Long = devAppsCustomUrlRepository.insertCustomUrl(customUrl)

    suspend fun insertCustomUrls(customUrls: List<CustomUrl>) = devAppsCustomUrlRepository.insertCustomUrls(customUrls)

    suspend fun updateCustomUrl(customUrl: CustomUrl): Int = devAppsCustomUrlRepository.updateCustomUrl(customUrl)

    suspend fun deleteCustomUrl(customUrl: CustomUrl) = devAppsCustomUrlRepository.deleteCustomUrl(customUrl)

    suspend fun deleteCustomUrls(customUrls: List<CustomUrl>) = devAppsCustomUrlRepository.deleteCustomUrls(customUrls)

    suspend fun deleteAll(): Int = devAppsCustomUrlRepository.deleteAllCustomUrl()
}