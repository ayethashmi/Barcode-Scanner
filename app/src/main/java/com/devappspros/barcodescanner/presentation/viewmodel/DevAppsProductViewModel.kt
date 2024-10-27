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
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.resources.Resource
import com.devappspros.barcodescanner.domain.usecases.DevAppsProductUseCase
import kotlinx.coroutines.launch

class DevAppsProductViewModel(private val devAppsProductUseCase: DevAppsProductUseCase): ViewModel() {
    val product: LiveData<Resource<DevAppsBarcodeAnalysis>> get() = devAppsProductUseCase.productObserver

    fun fetchProduct(barcode: Barcode, apiRemote: DevAppsRemoteAPI) = viewModelScope.launch {
        devAppsProductUseCase.fetchProduct(barcode, apiRemote)
    }

    override fun onCleared() {
        super.onCleared()
        devAppsProductUseCase.productObserver.value = null
    }
}