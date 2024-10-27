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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.databinding.FragmentBarcodeAboutContentsBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents.DevAppsDevAppsBarcodeMatrixAgendaFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents.DevAppsDevAppsBarcodeMatrixContactFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents.DevAppsDevAppsBarcodeMatrixEmailFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents.DevAppsDevAppsBarcodeMatrixLocalisationFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents.DevAppsDevAppsBarcodeMatrixPhoneFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents.DevAppsDevAppsBarcodeMatrixSmsFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents.DevAppsDevAppsBarcodeMatrixUriFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents.DevAppsDevAppsBarcodeMatrixWifiFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents.DevAppsDevAppsBarcodeTextFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.result.ParsedResult
import com.google.zxing.client.result.ParsedResultType
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class DevAppsDevAppsBarcodeAboutFragment: DevAppsBarcodeAnalysisFragment<DevAppsBarcodeAnalysis>() {

    private var _binding: FragmentBarcodeAboutContentsBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBarcodeAboutContentsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsBarcodeAnalysis) {
        val barcode = analysis.barcode

        val parsedResult: ParsedResult = get {
            parametersOf(barcode.contents, barcode.getBarcodeFormat())
        }

        val displayResult = parsedResult.displayResult

        configureHeaderEntitled(parsedResult, displayResult)
        configureHeaderIcon(barcode.getBarcodeFormat())
        configureBarcodeContentsFragment(parsedResult, displayResult)
    }

    private fun configureHeaderEntitled(parsedResult: ParsedResult, displayResult: String?) {
        val entitledStringResource: Int = if(displayResult.isNullOrBlank()){
            R.string.bar_code_content_label
        } else {
            when (parsedResult.type) {
                ParsedResultType.ADDRESSBOOK, ParsedResultType.EMAIL_ADDRESS, ParsedResultType.URI,
                ParsedResultType.GEO, ParsedResultType.TEL, ParsedResultType.SMS,
                ParsedResultType.CALENDAR, ParsedResultType.WIFI -> R.string.bar_code_analysis_label
                else -> R.string.bar_code_content_label
            }
        }

        viewBinding.fragmentBarcodeAboutContentsTitleTextView.text = getString(entitledStringResource)
    }

    private fun configureHeaderIcon(barcodeFormat: BarcodeFormat) {
        val barCodeIconDrawableResource: Int = when(barcodeFormat) {
            BarcodeFormat.QR_CODE -> R.drawable.baseline_qr_code_24
            BarcodeFormat.AZTEC -> R.drawable.ic_aztec_code_24
            BarcodeFormat.DATA_MATRIX -> R.drawable.ic_data_matrix_code_24
            BarcodeFormat.PDF_417 -> R.drawable.ic_pdf_417_code_24
            else -> R.drawable.ic_bar_code_24
        }

        viewBinding.fragmentBarcodeAboutContentsIconImageView.setImageResource(barCodeIconDrawableResource)
    }

    private fun configureBarcodeContentsFragment(parsedResult: ParsedResult, displayResult: String?) {

        val fragmentKClass = if(displayResult.isNullOrEmpty()) {
            DevAppsDevAppsBarcodeTextFragment::class
        } else {
            when (parsedResult.type) {
                ParsedResultType.TEXT -> DevAppsDevAppsBarcodeTextFragment::class
                ParsedResultType.ADDRESSBOOK -> DevAppsDevAppsBarcodeMatrixContactFragmentDevApps::class
                ParsedResultType.EMAIL_ADDRESS -> DevAppsDevAppsBarcodeMatrixEmailFragmentDevApps::class
                ParsedResultType.URI -> DevAppsDevAppsBarcodeMatrixUriFragmentDevApps::class
                ParsedResultType.GEO -> DevAppsDevAppsBarcodeMatrixLocalisationFragmentDevApps::class
                ParsedResultType.TEL -> DevAppsDevAppsBarcodeMatrixPhoneFragmentDevApps::class
                ParsedResultType.SMS -> DevAppsDevAppsBarcodeMatrixSmsFragmentDevApps::class
                ParsedResultType.CALENDAR -> DevAppsDevAppsBarcodeMatrixAgendaFragmentDevApps::class
                ParsedResultType.WIFI -> DevAppsDevAppsBarcodeMatrixWifiFragmentDevApps::class
                else -> DevAppsDevAppsBarcodeTextFragment::class
            }
        }

        applyFragment(
            containerViewId = viewBinding.fragmentBarcodeAboutContentsFrameLayout.id,
            fragmentClass = fragmentKClass,
            args = arguments
        )
    }
}