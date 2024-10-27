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

package com.devappspros.barcodescanner.presentation.views.activities

import com.devappspros.barcodescanner.common.utils.BARCODE_KEY
import com.devappspros.barcodescanner.common.utils.KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_RESULT
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.QrCodeErrorCorrectionLevel
import com.devappspros.barcodescanner.presentation.intent.createStartActivityIntent
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDatabaseBarcodeViewModel
import com.google.zxing.Result
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class DevAppsBarcodeScanFromImageShortcutActivityDevApps: DevAppsBarcodeScanFromImageGalleryActivityDevApps() {

    private val devAppsDatabaseBarcodeViewModel: DevAppsDatabaseBarcodeViewModel by viewModel()

    override fun onSuccessfulImageScan(result: Result?) {

        result?.let {
            val contents = result.text
            val formatName = result.barcodeFormat?.name

            if(contents != null && formatName != null){

                val errorCorrectionLevel: QrCodeErrorCorrectionLevel =
                    get(named(KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_RESULT)) { parametersOf(result) }

                val barcode: Barcode = get { parametersOf(contents, formatName, errorCorrectionLevel) }

                if(devAppsSettingsManager.shouldAddBarcodeScanToHistory) {
                    devAppsDatabaseBarcodeViewModel.insertBarcode(barcode, devAppsSettingsManager.saveDuplicates)
                }

                val intent = createStartActivityIntent(this, DevAppsBarcodeAnalysisActivityDevApps::class).apply {
                    putExtra(BARCODE_KEY, barcode)
                }

                startActivity(intent)

                finish()
            }
        }

    }
}