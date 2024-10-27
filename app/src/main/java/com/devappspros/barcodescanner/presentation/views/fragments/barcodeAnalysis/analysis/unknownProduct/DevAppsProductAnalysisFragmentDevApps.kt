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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.unknownProduct

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
import com.devappspros.barcodescanner.databinding.FragmentProductAnalysisBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPIError
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsUnknownProductDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeType
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeAnalysisActivityDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.about.DevAppsDevAppsBarcodeAboutOverviewFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.get

/**
 * A simple [Fragment] subclass.
 */
class DevAppsProductAnalysisFragmentDevApps : DevAppsBarcodeAnalysisFragment<DevAppsUnknownProductDevAppsBarcodeAnalysis>() {

    private var product: DevAppsUnknownProductDevAppsBarcodeAnalysis? = null

    private var _binding: FragmentProductAnalysisBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductAnalysisBinding.inflate(inflater, container, false)
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

                // On retire les menus inutile
                menu.removeItem(R.id.menu_activity_barcode_analysis_product_source_api_info_item)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId){

                R.id.menu_activity_barcode_analysis_download_from_apis -> {
                    product?.let { downloadFromRemoteAPI(it) }
                    true
                }

                R.id.menu_activity_barcode_analysis_about_barcode_item -> {
                    startBarcodeDetailsActivity()
                    true
                }
                else -> false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun start(analysis: DevAppsUnknownProductDevAppsBarcodeAnalysis) {

        this.product = analysis

        viewBinding.fragmentProductAnalysisOuterView.fixAnimateLayoutChangesInNestedScroll()

        configureBarcodeAboutOverviewFragment()

        analysis.apiError.let { apiError ->
            when(apiError){
                DevAppsRemoteAPIError.NO_INTERNET_PERMISSION, DevAppsRemoteAPIError.ERROR -> {
                    configureBarcodeErrorApiFragment()
                }
                DevAppsRemoteAPIError.AUTOMATIC_API_RESEARCH_DISABLED -> {
                    viewBinding.fragmentProductAnalysisSearchApiEntitledLayout.visibility = View.GONE
                    viewBinding.fragmentProductAnalysisSearchApiFrameLayout.visibility = View.GONE
                }
                DevAppsRemoteAPIError.NO_RESULT -> {
                    configureBarcodeNotFoundApi(analysis)
                }
            }
        }
    }

    private fun configureBarcodeAboutOverviewFragment() = applyFragment(
        containerViewId = viewBinding.fragmentProductAnalysisBarcodeAboutOverviewFrameLayout.id,
        fragmentClass = DevAppsDevAppsBarcodeAboutOverviewFragment::class,
        args = arguments
    )

    /**
     * Si une erreur s'est produit pendant la recherche sur les APIs
     */
    private fun configureBarcodeErrorApiFragment() = applyFragment(
        containerViewId = viewBinding.fragmentProductAnalysisSearchApiFrameLayout.id,
        fragmentClass = DevAppsProductAnalysisErrorFragmentDevApps::class,
        args = arguments
    )

    /**
     * Si pas d'erreur mais pas trouvé dans les APIs distantes
     */
    private fun configureBarcodeNotFoundApi(devAppsBarcodeAnalysis: DevAppsBarcodeAnalysis) {
        val apiRemote = getString(devAppsBarcodeAnalysis.source.nameResource)
        configureBarcodeNotFoundApiFragment(getString(R.string.barcode_not_found_on_api_label, apiRemote))
    }

    private fun configureBarcodeNotFoundApiFragment(contents: String) = configureExpandableCardViewFragment(
        frameLayout = viewBinding.fragmentProductAnalysisSearchApiFrameLayout,
        title = getString(R.string.information_label),
        contents = contents,
        iconDrawableResource = R.drawable.outline_info_24
    )

    private fun downloadFromRemoteAPI(product: DevAppsUnknownProductDevAppsBarcodeAnalysis) {
        requireActivity().apply {
            if(this is DevAppsBarcodeAnalysisActivityDevApps) {
                val barcode = product.barcode
                when(product.apiError) {
                    DevAppsRemoteAPIError.NO_RESULT -> {
                        if (!barcode.isBookBarcode()) {
                            createRemoteApiAlertDialog(barcode, this)
                        }
                    }
                    DevAppsRemoteAPIError.AUTOMATIC_API_RESEARCH_DISABLED -> {
                        when(barcode.getBarcodeType()) {
                            BarcodeType.FOOD -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_FOOD_FACTS)
                            BarcodeType.BEAUTY -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_BEAUTY_FACTS)
                            BarcodeType.PET_FOOD -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_PET_FOOD_FACTS)
                            BarcodeType.MUSIC -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.MUSICBRAINZ)
                            BarcodeType.BOOK -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_LIBRARY)
                            else -> createRemoteApiAlertDialog(barcode, this)
                        }
                    }
                    else -> {
                        when(barcode.getBarcodeType()) {
                            BarcodeType.FOOD -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_FOOD_FACTS)
                            BarcodeType.BEAUTY -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_BEAUTY_FACTS)
                            BarcodeType.PET_FOOD -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_PET_FOOD_FACTS)
                            BarcodeType.MUSIC -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.MUSICBRAINZ)
                            BarcodeType.BOOK -> this.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_LIBRARY)
                            else -> createRemoteApiAlertDialog(barcode, this)
                        }
                    }
                }
            }
        }
    }

    private fun createRemoteApiAlertDialog(barcode: Barcode, devAppsBarcodeAnalysisActivity: DevAppsBarcodeAnalysisActivityDevApps) {
        val items = arrayOf(
            getString(R.string.preferences_remote_api_food_label),
            getString(R.string.preferences_remote_api_cosmetic_label),
            getString(R.string.preferences_remote_api_pet_food_label),
            getString(R.string.preferences_remote_api_musicbrainz_label)
        )

        val builder = MaterialAlertDialogBuilder(devAppsBarcodeAnalysisActivity).apply {
            setTitle(R.string.preferences_remote_api_choose_label)
            setItems(items) { _, i ->
                when(i) {
                    0 -> devAppsBarcodeAnalysisActivity.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_FOOD_FACTS)
                    1 -> devAppsBarcodeAnalysisActivity.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_BEAUTY_FACTS)
                    2 -> devAppsBarcodeAnalysisActivity.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.OPEN_PET_FOOD_FACTS)
                    3 -> devAppsBarcodeAnalysisActivity.fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.MUSICBRAINZ)
                }
            }
            setNegativeButton(R.string.close_dialog_label) {
                    dialogInterface, _ -> dialogInterface.cancel()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        fun newInstance(barcodeAnalysis: DevAppsUnknownProductDevAppsBarcodeAnalysis) =
            DevAppsProductAnalysisFragmentDevApps().apply {
                arguments = get<Bundle>().apply {
                    putSerializable(BARCODE_ANALYSIS_KEY, barcodeAnalysis)
                }
            }
    }
}