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

package com.devappspros.barcodescanner.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devappspros.barcodescanner.domain.entity.customUrl.CustomUrl
import com.devappspros.barcodescanner.domain.usecases.DevAppsDatabaseCustomUrlUseCase
import kotlinx.coroutines.launch

class DevAppsDatabaseCustomUrlViewModel(private val devAppsDatabaseCustomUrlUseCase: DevAppsDatabaseCustomUrlUseCase): ViewModel() {

    val customUrlList: LiveData<List<CustomUrl>> = devAppsDatabaseCustomUrlUseCase.customUrlList

    fun insertCustomUrl(customUrl: CustomUrl) = viewModelScope.launch {
        devAppsDatabaseCustomUrlUseCase.insertCustomUrl(customUrl)
    }

    fun insertCustomUrls(customUrls: List<CustomUrl>) = viewModelScope.launch {
        devAppsDatabaseCustomUrlUseCase.insertCustomUrls(customUrls)
    }

    fun updateCustomUrl(customUrl: CustomUrl) = viewModelScope.launch {
        devAppsDatabaseCustomUrlUseCase.updateCustomUrl(customUrl)
    }

    fun deleteCustomUrl(customUrl: CustomUrl) = viewModelScope.launch {
        devAppsDatabaseCustomUrlUseCase.deleteCustomUrl(customUrl)
    }

    fun deleteCustomUrls(customUrls: List<CustomUrl>) = viewModelScope.launch {
        devAppsDatabaseCustomUrlUseCase.deleteCustomUrls(customUrls)
    }

    fun deleteAll() = viewModelScope.launch {
        devAppsDatabaseCustomUrlUseCase.deleteAll()
    }
}