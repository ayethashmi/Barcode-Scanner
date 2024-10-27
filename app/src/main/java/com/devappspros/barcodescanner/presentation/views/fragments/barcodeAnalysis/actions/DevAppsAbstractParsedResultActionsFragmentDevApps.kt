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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions

import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.google.zxing.client.result.ParsedResult
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

abstract class DevAppsAbstractParsedResultActionsFragmentDevApps: DevAppsAbstractActionsFragmentDevApps() {

    override fun configureActionItems(barcode: Barcode) {
        configureActionItems(
            barcode = barcode,
            parsedResult = get {
                parametersOf(barcode.contents, barcode.getBarcodeFormat())
            }
        )
    }

    abstract fun configureActionItems(barcode: Barcode, parsedResult: ParsedResult)
}