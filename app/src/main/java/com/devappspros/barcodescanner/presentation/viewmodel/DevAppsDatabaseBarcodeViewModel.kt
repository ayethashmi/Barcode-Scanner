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

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devappspros.barcodescanner.domain.entity.DevAppsFileFormat
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeType
import com.devappspros.barcodescanner.domain.resources.Resource
import com.devappspros.barcodescanner.domain.usecases.DevAppsDatabaseBarcodeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DevAppsDatabaseBarcodeViewModel(private val devAppsDatabaseBarcodeUseCase: DevAppsDatabaseBarcodeUseCase): ViewModel() {

    val barcodeList: LiveData<List<Barcode>> = devAppsDatabaseBarcodeUseCase.barcodeList

    fun getBarcodeByDate(date: Long): LiveData<Barcode?> = devAppsDatabaseBarcodeUseCase.getBarcodeByDate(date)

    fun insertBarcode(barcode: Barcode, saveDuplicates: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if(saveDuplicates) {
            devAppsDatabaseBarcodeUseCase.insertBarcode(barcode)
        } else {
            if(!devAppsDatabaseBarcodeUseCase.isExists(barcode.contents, barcode.formatName)) {
                devAppsDatabaseBarcodeUseCase.insertBarcode(barcode)
            }
        }
    }

    fun insertBarcodes(barcodes: List<Barcode>) = viewModelScope.launch {
        devAppsDatabaseBarcodeUseCase.insertBarcodes(barcodes)
    }

    fun update(date: Long, contents: String, barcodeType: BarcodeType, name: String) = viewModelScope.launch {
        devAppsDatabaseBarcodeUseCase.update(date, contents, barcodeType, name)
    }

    fun updateType(date: Long, barcodeType: BarcodeType) = viewModelScope.launch {
        devAppsDatabaseBarcodeUseCase.updateType(date, barcodeType)
    }

    fun updateTypeAndName(date: Long, barcodeType: BarcodeType, name: String) = viewModelScope.launch {
        devAppsDatabaseBarcodeUseCase.updateTypeAndName(date, barcodeType, name)
    }

    fun deleteBarcode(barcode: Barcode) = viewModelScope.launch {
        devAppsDatabaseBarcodeUseCase.deleteBarcode(barcode)
    }

    fun deleteBarcodes(barcodes: List<Barcode>) = viewModelScope.launch {
        devAppsDatabaseBarcodeUseCase.deleteBarcodes(barcodes)
    }

    fun deleteAll() = viewModelScope.launch {
        devAppsDatabaseBarcodeUseCase.deleteAll()
    }

    fun exportToFile(
        barcodes: List<Barcode>,
        format: DevAppsFileFormat,
        uri: Uri
    ): LiveData<Resource<Boolean>> {
        return when(format) {
            DevAppsFileFormat.CSV -> devAppsDatabaseBarcodeUseCase.exportToCsv(barcodes, uri)
            DevAppsFileFormat.JSON -> devAppsDatabaseBarcodeUseCase.exportToJson(barcodes, uri)
        }
    }

    fun importFile(uri: Uri): LiveData<Resource<Boolean>> {
        return devAppsDatabaseBarcodeUseCase.importFromJson(uri)
    }
}