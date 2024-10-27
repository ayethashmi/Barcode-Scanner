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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.devappspros.barcodescanner.common.extensions.serializable
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_CONTENTS_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_FORMAT_KEY
import com.devappspros.barcodescanner.common.utils.QR_CODE_ERROR_CORRECTION_LEVEL_KEY
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.presentation.intent.createStartActivityIntent
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeDetailsActivityDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.BaseFragment
import com.devappspros.barcodescanner.presentation.views.fragments.templates.ExpandableCardViewFragment

abstract class DevAppsBarcodeAnalysisFragment<T: DevAppsBarcodeAnalysis>: BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureMenu()

        arguments?.takeIf {
            // Si les données ResultScanData sont bien stockées en mémoire
            it.containsKey(BARCODE_ANALYSIS_KEY)// && it.getSerializable(PRODUCT_KEY) is BarcodeProduct
        }?.apply {

            serializable(BARCODE_ANALYSIS_KEY, DevAppsBarcodeAnalysis::class.java)?.let { barcodeAnalysis ->
                try {
                    @Suppress("UNCHECKED_CAST")
                    start(barcodeAnalysis as T)
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }
            }
        }
    }

    protected open fun configureMenu() {}

    abstract fun start(analysis: T)

    protected fun configureExpandableCardViewFragment(
        frameLayout: FrameLayout,
        title: String,
        contents: CharSequence?,
        iconDrawableResource: Int? = null
    ) {
        if(!contents.isNullOrBlank()) {
            applyFragment(
                containerViewId = frameLayout.id,
                fragment = ExpandableCardViewFragment.newInstance(
                    title = title,
                    contents = contents.trim(),
                    drawableResource = iconDrawableResource
                )
            )
        } else {
            frameLayout.visibility = View.GONE
        }
    }

    protected fun startBarcodeDetailsActivity() {
        arguments?.serializable(BARCODE_ANALYSIS_KEY, DevAppsBarcodeAnalysis::class.java)?.let { barcodeAnalysis ->
            val intent = createStartActivityIntent(requireContext(), DevAppsBarcodeDetailsActivityDevApps::class).apply {
                putExtra(BARCODE_CONTENTS_KEY, barcodeAnalysis.barcode.contents)
                putExtra(BARCODE_FORMAT_KEY, barcodeAnalysis.barcode.formatName)
                putExtra(QR_CODE_ERROR_CORRECTION_LEVEL_KEY, barcodeAnalysis.barcode.errorCorrectionLevel)
            }
            startActivity(intent)
        }
    }
}