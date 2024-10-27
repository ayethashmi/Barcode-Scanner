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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.defaultAnalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.fixAnimateLayoutChangesInNestedScroll
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.databinding.FragmentDefaultBarcodeAnalysisBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsDefaultDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.about.DevAppsDevAppsBarcodeAboutOverviewFragment
import org.koin.android.ext.android.get

/**
 * A simple [Fragment] subclass.
 */
class DevAppsDefaultDevAppsBarcodeAnalysisFragment : DevAppsBarcodeAnalysisFragment<DevAppsDefaultDevAppsBarcodeAnalysis>() {
    private var _binding: FragmentDefaultBarcodeAnalysisBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDefaultBarcodeAnalysisBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun configureMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_activity_barcode_analysis, menu)

                // On retire les menus inutiles
                menu.removeItem(R.id.menu_activity_barcode_analysis_download_from_apis)
                menu.removeItem(R.id.menu_activity_barcode_analysis_product_source_api_info_item)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
                R.id.menu_activity_barcode_analysis_about_barcode_item -> {
                    startBarcodeDetailsActivity()
                    true
                }
                else -> false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun start(analysis: DevAppsDefaultDevAppsBarcodeAnalysis) {
        viewBinding.fragmentDefaultBarcodeAnalysisOuterView.fixAnimateLayoutChangesInNestedScroll()
        configureBarcodeAboutOverviewFragment()
    }

    private fun configureBarcodeAboutOverviewFragment() = applyFragment(
        containerViewId = viewBinding.fragmentDefaultBarcodeAnalysisBarcodeAboutOverviewFrameLayout.id,
        fragmentClass = DevAppsDevAppsBarcodeAboutOverviewFragment::class,
        args = arguments
    )

    companion object {
        fun newInstance(barcodeAnalysis: DevAppsDefaultDevAppsBarcodeAnalysis) = DevAppsDefaultDevAppsBarcodeAnalysisFragment().apply {
            arguments = get<Bundle>().apply {
                putSerializable(BARCODE_ANALYSIS_KEY, barcodeAnalysis)
            }
        }
    }
}