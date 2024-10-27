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

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeType
import com.devappspros.barcodescanner.domain.repositories.DevAppsBarcodeRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsFileStreamRepository
import com.devappspros.barcodescanner.domain.resources.Resource
import kotlinx.coroutines.Dispatchers

class DevAppsDatabaseBarcodeUseCase(
    private val devAppsBarcodeRepository: DevAppsBarcodeRepository,
    private val devAppsFileStreamRepository: DevAppsFileStreamRepository
) {

    val barcodeList: LiveData<List<Barcode>> = devAppsBarcodeRepository.getBarcodeList()

    fun getBarcodeByDate(date: Long): LiveData<Barcode?> = devAppsBarcodeRepository.getBarcodeByDate(date)

    suspend fun isExists(contents: String, format: String): Boolean = devAppsBarcodeRepository.isExists(contents, format)

    suspend fun insertBarcode(barcode: Barcode): Long = devAppsBarcodeRepository.insertBarcode(barcode)

    suspend fun insertBarcodes(barcodes: List<Barcode>) = devAppsBarcodeRepository.insertBarcodes(barcodes)

    suspend fun update(date: Long, contents: String, barcodeType: BarcodeType, name: String): Int =
        devAppsBarcodeRepository.update(date, contents, barcodeType, name)

    suspend fun updateType(date: Long, barcodeType: BarcodeType): Int =
        devAppsBarcodeRepository.updateType(date, barcodeType)

    suspend fun updateTypeAndName(date: Long, barcodeType: BarcodeType, name: String): Int =
        devAppsBarcodeRepository.updateTypeAndName(date, barcodeType, name)

    suspend fun deleteBarcode(barcode: Barcode) = devAppsBarcodeRepository.deleteBarcode(barcode)

    suspend fun deleteBarcodes(barcodes: List<Barcode>) = devAppsBarcodeRepository.deleteBarcodes(barcodes)

    suspend fun deleteAll(): Int = devAppsBarcodeRepository.deleteAllBarcode()

    fun exportToCsv(
        barcodes: List<Barcode>,
        uri: Uri
    ): LiveData<Resource<Boolean>> {
        return doAction {
            devAppsFileStreamRepository.exportToCsv(barcodes, uri)
        }
    }

    fun exportToJson(
        barcodes: List<Barcode>,
        uri: Uri
    ): LiveData<Resource<Boolean>> {
        return doAction {
            devAppsFileStreamRepository.exportToJson(barcodes, uri)
        }
    }

    fun importFromJson(uri: Uri): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        val barcodes: List<Barcode>? = devAppsFileStreamRepository.importFromJson(uri)

        if(barcodes.isNullOrEmpty()) {
            emit(Resource.success(false))
        } else {
            try {
                insertBarcodes(barcodes)
                emit(Resource.success(true))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.failure(e, false))
            }
        }
    }

    private fun doAction(
        export: () -> Boolean
    ): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val success = export()
            emit(Resource.success(success))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, false))
        }
    }
}