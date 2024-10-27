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

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devappspros.barcodescanner.common.extensions.fixAnimateLayoutChangesInNestedScroll
import com.devappspros.barcodescanner.common.utils.BARCODE_IMAGE_DEFAULT_SIZE
import com.devappspros.barcodescanner.databinding.FragmentBarcodeAboutOverviewBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeType
import com.devappspros.barcodescanner.domain.library.DevAppsBarcodeImageGeneratorProperties
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsAbstractActionsFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeImageEditor.DevAppsBarcodeImageFragment
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

/**
 * Contains:
 * - Barcode content (BarcodeAboutContentsFragment)
 * - Additional information (BarcodeAboutMoreInfoFragment)
 * - Actions (subclass of AbstractActionsFragment)
 */
class DevAppsDevAppsBarcodeAboutOverviewFragment : DevAppsBarcodeAnalysisFragment<DevAppsBarcodeAnalysis>() {

    private var _binding: FragmentBarcodeAboutOverviewBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBarcodeAboutOverviewBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsBarcodeAnalysis) {
        viewBinding.root.fixAnimateLayoutChangesInNestedScroll()
        configureBarcodeAboutImageFragment(analysis.barcode)
        configureBarcodeAboutContentsFragment()
        configureBarcodeAboutMoreInfoFragment()
        configureBarcodeActionsFragment(analysis.barcode.getBarcodeType())
    }

    private fun configureBarcodeAboutImageFragment(barcode: Barcode) {
        if(devAppsSettingsManager.shouldDisplayBarcodeInResultsView) {
            val properties = DevAppsBarcodeImageGeneratorProperties(
                contents = barcode.contents,
                format = barcode.getBarcodeFormat(),
                qrCodeErrorCorrectionLevel = barcode.getQrCodeErrorCorrectionLevel(),
                size = BARCODE_IMAGE_DEFAULT_SIZE,
                frontColor = Color.BLACK,
                backgroundColor = Color.WHITE
            )

            /*applyFragment(
                containerViewId = viewBinding.fragmentBarcodeAboutOverviewBarcodeImageFrameLayout.id,
                fragmentClass = BarcodeImageFragment::class,
                args = Bundle().apply {
                    putSerializable(BARCODE_IMAGE_GENERATOR_PROPERTIES_KEY, properties)
                }
            )*/

            applyFragment(
                containerViewId = viewBinding.fragmentBarcodeAboutOverviewBarcodeImageFrameLayout.id,
                fragment = DevAppsBarcodeImageFragment.newInstance(properties)
            )
        } else {
            viewBinding.fragmentBarcodeAboutOverviewBarcodeImageFrameLayout.visibility = View.GONE
        }
    }

    private fun configureBarcodeAboutContentsFragment() = applyFragment(
        containerViewId = viewBinding.fragmentBarcodeAboutOverviewBarcodeContentsFrameLayout.id,
        fragmentClass = DevAppsDevAppsBarcodeAboutFragment::class,
        args = arguments
    )

    private fun configureBarcodeAboutMoreInfoFragment() = applyFragment(
        containerViewId = viewBinding.fragmentBarcodeAboutOverviewMoreInfoFrameLayout.id,
        fragmentClass = DevAppsDevAppsBarcodeAboutMoreInfoFragment::class,
        args = arguments
    )

    private fun configureBarcodeActionsFragment(barcodeType: BarcodeType) = applyFragment(
        containerViewId = viewBinding.fragmentBarcodeAboutOverviewBarcodeActionsFrameLayout.id,
        fragmentClass = get<KClass<out DevAppsAbstractActionsFragmentDevApps>> { parametersOf(barcodeType) },
        args = arguments
    )
}