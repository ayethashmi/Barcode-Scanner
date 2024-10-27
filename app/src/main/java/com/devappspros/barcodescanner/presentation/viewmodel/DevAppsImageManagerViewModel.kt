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

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.devappspros.barcodescanner.domain.library.DevAppsBarcodeImageGeneratorProperties
import com.devappspros.barcodescanner.domain.resources.Resource
import com.devappspros.barcodescanner.domain.usecases.DevAppsImageManagerUseCase
import kotlinx.coroutines.launch

class DevAppsImageManagerViewModel(private val devAppsImageManagerUseCase: DevAppsImageManagerUseCase): ViewModel() {
    fun getBitmap(): LiveData<Resource<Bitmap?>> = devAppsImageManagerUseCase.bitmapObserver

    fun createBitmap(properties: DevAppsBarcodeImageGeneratorProperties) = viewModelScope.launch {
        devAppsImageManagerUseCase.createBitmap(properties)
    }

    fun createSvg(properties: DevAppsBarcodeImageGeneratorProperties): LiveData<String?> {
        return devAppsImageManagerUseCase.createSvg(properties)
    }

    fun exportAsPng(bitmap: Bitmap?, uri: Uri): LiveData<Resource<Boolean>> {
        return devAppsImageManagerUseCase.exportToPng(bitmap, uri)
    }

    fun exportAsJpg(bitmap: Bitmap?, uri: Uri): LiveData<Resource<Boolean>> {
        return devAppsImageManagerUseCase.exportToJpg(bitmap, uri)
    }

    fun exportAsSvg(properties: DevAppsBarcodeImageGeneratorProperties, uri: Uri): LiveData<Resource<Boolean>> {
        return createSvg(properties).switchMap { svg: String? ->
            devAppsImageManagerUseCase.exportToSvg(svg, uri)
        }
    }

    fun shareBitmap(bitmap: Bitmap?): LiveData<Resource<Uri?>> {
        return devAppsImageManagerUseCase.shareBitmap(bitmap)
    }

    override fun onCleared() {
        super.onCleared()
        devAppsImageManagerUseCase.bitmapObserver.value = null
    }
}