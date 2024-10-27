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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.utils.BARCODE_CONTENTS_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_FORMAT_KEY
import com.devappspros.barcodescanner.common.utils.QR_CODE_ERROR_CORRECTION_LEVEL_KEY
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.QrCodeErrorCorrectionLevel
import com.devappspros.barcodescanner.domain.library.DevAppsBarcodeFormatChecker
import com.devappspros.barcodescanner.presentation.intent.createStartActivityIntent
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDatabaseBarcodeViewModel
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeDetailsActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeFormCreatorActivityDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.BaseFragment
import com.google.zxing.BarcodeFormat
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf

abstract class DevAppsAbstractBarcodeFormCreatorFragment: BaseFragment() {

    private val devAppsDatabaseBarcodeViewModel: DevAppsDatabaseBarcodeViewModel by activityViewModel()
    protected val devAppsBarcodeFormatChecker: DevAppsBarcodeFormatChecker by inject()

    protected fun configureMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_activity_confirm, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
                R.id.menu_activity_confirm_item -> {
                    generateBarcode()
                    true
                }
                else -> false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    protected fun startBarcodeDetailsActivity(
        contents: String,
        barcodeFormat: BarcodeFormat,
        qrCodeErrorCorrectionLevel: QrCodeErrorCorrectionLevel = QrCodeErrorCorrectionLevel.NONE
    ) {
        insertBarcodeIntoDatabase(contents, barcodeFormat, qrCodeErrorCorrectionLevel)
        val intent = createStartActivityIntent(requireContext(), DevAppsBarcodeDetailsActivityDevApps::class).apply {
            putExtra(BARCODE_CONTENTS_KEY, contents)
            putExtra(BARCODE_FORMAT_KEY, barcodeFormat.name)
            putExtra(QR_CODE_ERROR_CORRECTION_LEVEL_KEY, qrCodeErrorCorrectionLevel.name)
        }
        startActivity(intent)
    }

    private fun insertBarcodeIntoDatabase(
        contents: String,
        barcodeFormat: BarcodeFormat,
        qrCodeErrorCorrectionLevel: QrCodeErrorCorrectionLevel
    ) {
        if(devAppsSettingsManager.shouldAddBarcodeGenerateToHistory) {
            val barcode: Barcode = get {
                parametersOf(contents, barcodeFormat.name, qrCodeErrorCorrectionLevel)
            }
            // Insert les informations du code-barres dans la base de données (de manière asynchrone)
            devAppsDatabaseBarcodeViewModel.insertBarcode(barcode, devAppsSettingsManager.saveDuplicates)
        }
    }

    protected fun configureErrorMessage(message: String) {
        val activity = requireActivity()
        if(activity is DevAppsBarcodeFormCreatorActivityDevApps){
            activity.configureErrorMessage(message)
        }
    }

    protected fun hideErrorMessage() {
        val activity = requireActivity()
        if(activity is DevAppsBarcodeFormCreatorActivityDevApps){
            activity.hideErrorMessage()
        }
    }

    protected fun generateBarcode() {
        val contents = getBarcodeTextFromForm()
        checkError(contents)?.let {
            configureErrorMessage(it)
        } ?: run {
            hideSoftKeyboard()
            hideErrorMessage()
            startBarcodeDetailsActivity(contents, getBarcodeFormat(), getQrCodeErrorCorrectionLevel())
        }
    }

    abstract val checkError: (contents: String) -> String?
    abstract fun getBarcodeTextFromForm(): String
    abstract fun getBarcodeFormat(): BarcodeFormat
    abstract fun getQrCodeErrorCorrectionLevel(): QrCodeErrorCorrectionLevel
}