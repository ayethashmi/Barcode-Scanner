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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeImageEditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.fixAnimateLayoutChangesInNestedScroll
import com.devappspros.barcodescanner.common.extensions.serializable
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_CONTENTS_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_FORMAT_KEY
import com.devappspros.barcodescanner.common.utils.QR_CODE_ERROR_CORRECTION_LEVEL_KEY
import com.devappspros.barcodescanner.databinding.FragmentBarcodeInfoBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsDefaultDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.QrCodeErrorCorrectionLevel
import com.devappspros.barcodescanner.presentation.views.fragments.BaseFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.about.DevAppsDevAppsBarcodeAboutMoreInfoFragment
import com.devappspros.barcodescanner.presentation.views.fragments.templates.ExpandableCardViewFragment
import com.google.zxing.BarcodeFormat
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class DevAppsBarcodeInfoFragment : BaseFragment() {

    private var _binding: FragmentBarcodeInfoBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBarcodeInfoBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.fragmentBarcodeInfoOuterView.fixAnimateLayoutChangesInNestedScroll()
        arguments?.let {
            val contents: String = it.getString(BARCODE_CONTENTS_KEY) ?: ""
            val format: BarcodeFormat = it.serializable(BARCODE_FORMAT_KEY, BarcodeFormat::class.java) ?: BarcodeFormat.QR_CODE
            val qrCodeErrorCorrectionLevel: QrCodeErrorCorrectionLevel = it.serializable(QR_CODE_ERROR_CORRECTION_LEVEL_KEY, QrCodeErrorCorrectionLevel::class.java) ?: QrCodeErrorCorrectionLevel.NONE

            configureBarcodeContentsExpandableCardViewFragment(contents, format)
            configureBarcodeAboutMoreInfoFragment(contents, format, qrCodeErrorCorrectionLevel)
        }
    }

    private fun configureBarcodeContentsExpandableCardViewFragment(
        contents: String,
        format: BarcodeFormat
    ) {
        val iconResource: Int = when(format) {
            BarcodeFormat.QR_CODE -> R.drawable.baseline_qr_code_24
            BarcodeFormat.AZTEC -> R.drawable.ic_aztec_code_24
            BarcodeFormat.DATA_MATRIX -> R.drawable.ic_data_matrix_code_24
            BarcodeFormat.PDF_417 -> R.drawable.ic_pdf_417_code_24
            else -> R.drawable.ic_bar_code_24
        }

        applyFragment(
            containerViewId = viewBinding.fragmentBarcodeInfoContentsFrameLayout.id,
            fragment = ExpandableCardViewFragment.newInstance(
                title = getString(R.string.bar_code_content_label),
                contents = contents,
                drawableResource = iconResource
            )
        )
    }

    private fun configureBarcodeAboutMoreInfoFragment(
        contents: String,
        format: BarcodeFormat,
        qrCodeErrorCorrectionLevel: QrCodeErrorCorrectionLevel
    ) {
        val barcode: Barcode = get { parametersOf(contents, format.name, qrCodeErrorCorrectionLevel) }
        val barcodeAnalysis = DevAppsDefaultDevAppsBarcodeAnalysis(barcode)

        val args: Bundle = get<Bundle>().apply {
            putSerializable(BARCODE_ANALYSIS_KEY, barcodeAnalysis)
        }

        applyFragment(
            containerViewId = viewBinding.fragmentBarcodeInfoMoreInfoFrameLayout.id,
            fragmentClass = DevAppsDevAppsBarcodeAboutMoreInfoFragment::class,
            args = args
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(
            contents: String,
            format: BarcodeFormat,
            errorCorrectionLevel: QrCodeErrorCorrectionLevel
        ) = DevAppsBarcodeInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(BARCODE_CONTENTS_KEY, contents)
                    putSerializable(BARCODE_FORMAT_KEY, format)
                    putSerializable(QR_CODE_ERROR_CORRECTION_LEVEL_KEY, errorCorrectionLevel)
                }
            }
    }
}