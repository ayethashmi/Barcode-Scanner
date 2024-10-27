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

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.convertToString
import com.devappspros.barcodescanner.common.extensions.getColorInt
import com.devappspros.barcodescanner.common.extensions.serializable
import com.devappspros.barcodescanner.common.utils.BARCODE_KEY
import com.devappspros.barcodescanner.databinding.ActivityBarcodeAnalysisBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBookDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsDefaultDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsMusicDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPI
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsRemoteAPIError
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsUnknownProductDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeType
import com.devappspros.barcodescanner.domain.resources.Resource
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDatabaseBarcodeViewModel
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsProductViewModel
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.bookProduct.DevAppsBookAnalysisFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.defaultAnalysis.DevAppsDefaultDevAppsBarcodeAnalysisFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.foodProduct.DevAppsFoodAnalysisFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.musicProduct.DevAppsMusicAnalysisFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.unknownProduct.DevAppsProductAnalysisFragmentDevApps
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DevAppsBarcodeAnalysisActivityDevApps: DevAppsBaseActivity() {

    // ---- Views ----

    private val viewBinding: ActivityBarcodeAnalysisBinding by lazy {
        ActivityBarcodeAnalysisBinding.inflate(layoutInflater)
    }
    override val rootView: View get() = viewBinding.root

    // ---- ViewModel ----

    private val devAppsDatabaseBarcodeViewModel by viewModel<DevAppsDatabaseBarcodeViewModel>()
    private val retrofitViewModel by viewModel<DevAppsProductViewModel>()

    // ----

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(viewBinding.activityBarcodeAnalysisActivityLayout.toolbar)

        intent?.serializable(BARCODE_KEY, Barcode::class.java)?.let { barcode: Barcode ->
            configureContentsView(barcode)
        }

        setContentView(rootView)
    }

    private fun configureContentsView(barcode: Barcode) {
        changeToolbarText(barcode)
        when {
            barcode.is1DProductBarcodeFormat -> configureProductRemoteAPI(barcode)
            else -> configureDefaultBarcodeAnalysisView(DevAppsDefaultDevAppsBarcodeAnalysis(barcode))
        }
    }

    private fun configureProductRemoteAPI(barcode: Barcode) {

        // Check Internet Permission
        if(!isInternetPermissionGranted()) {
            configureUnknownProductAnalysisView(
                barcodeAnalysis = DevAppsUnknownProductDevAppsBarcodeAnalysis(
                    barcode = barcode,
                    apiError = DevAppsRemoteAPIError.NO_INTERNET_PERMISSION,
                    message = getString(R.string.no_internet_permission)
                )
            )
            return
        }

        observeProductFromAPI()
        if(devAppsSettingsManager.useSearchOnApi) {
            fetchProductFromRemoteAPI(barcode, determineAPIRemote(barcode.getBarcodeType()))
        } else {
            fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.NONE)
        }
    }

    // ---- Query remote API ----
    private fun observeProductFromAPI() {
        retrofitViewModel.product.observe(this) {

            when (it) {

                is Resource.Progress -> {
                    viewBinding.activityBarcodeAnalysisProgressBar.visibility = View.VISIBLE
                }

                is Resource.Failure -> {
                    viewBinding.activityBarcodeAnalysisProgressBar.visibility = View.GONE
                    configureUnknownProductAnalysisView(
                        barcodeAnalysis = it.data as? DevAppsUnknownProductDevAppsBarcodeAnalysis
                            ?: DevAppsUnknownProductDevAppsBarcodeAnalysis(
                                barcode = it.data.barcode,
                                apiError = DevAppsRemoteAPIError.ERROR,
                                message = it.throwable.toString(),
                                source = it.data.source
                            ),
                    )
                }

                is Resource.Success -> {
                    viewBinding.activityBarcodeAnalysisProgressBar.visibility = View.GONE
                    when (it.data) {
                        is DevAppsFoodDevAppsBarcodeAnalysis -> configureFoodAnalysisView(it.data)
                        is DevAppsMusicDevAppsBarcodeAnalysis -> configureMusicAnalysisView(it.data)
                        is DevAppsBookDevAppsBarcodeAnalysis -> configureBookAnalysisView(it.data)
                        is DevAppsUnknownProductDevAppsBarcodeAnalysis -> configureUnknownProductAnalysisView(it.data)
                        else -> configureUnknownProductAnalysisView(
                            barcodeAnalysis = DevAppsUnknownProductDevAppsBarcodeAnalysis(
                                barcode = it.data.barcode,
                                apiError = DevAppsRemoteAPIError.NO_RESULT,
                                source = it.data.source
                            )
                        )
                    }
                }

                else -> {
                    viewBinding.activityBarcodeAnalysisProgressBar.visibility = View.GONE
                }
            }
        }
    }

    fun fetchProductFromRemoteAPI(barcode: Barcode, apiRemote: DevAppsRemoteAPI? = null) {
        if(isInternetPermissionGranted()) {
            retrofitViewModel.fetchProduct(
                barcode = barcode,
                apiRemote = apiRemote ?: determineAPIRemote(barcode.getBarcodeType())
            )
        }
    }

    // ---- Configuration de la vue principale en fonction du type de code-barres / produits ----

    private fun configureFoodAnalysisView(
        barcodeAnalysis: DevAppsFoodDevAppsBarcodeAnalysis
    ) {
        // On supprime le comportement de changement de couleur de la Top Bar lors du scroll, car cette vue contient un TabLayout qui crée un contraste étrange.
        viewBinding.activityBarcodeAnalysisActivityLayout.appBarLayout.setBackgroundColor(this.getColorInt(android.R.attr.colorBackground))

        updateTypeIntoDatabase(devAppsBarcodeAnalysis = barcodeAnalysis)
        configureContentFragment(
            fragment = DevAppsFoodAnalysisFragmentDevAppsDevApps.newInstance(barcodeAnalysis),
            devAppsBarcodeAnalysis = barcodeAnalysis
        )
    }

    private fun configureMusicAnalysisView(
        barcodeAnalysis: DevAppsMusicDevAppsBarcodeAnalysis
    ) {
        updateTypeIntoDatabase(devAppsBarcodeAnalysis = barcodeAnalysis)
        configureContentFragment(
            fragment = DevAppsMusicAnalysisFragmentDevAppsDevApps.newInstance(barcodeAnalysis),
            devAppsBarcodeAnalysis = barcodeAnalysis
        )
    }

    private fun configureBookAnalysisView(
        barcodeAnalysis: DevAppsBookDevAppsBarcodeAnalysis
    ) {
        updateTypeIntoDatabase(devAppsBarcodeAnalysis = barcodeAnalysis)
        configureContentFragment(
            fragment = DevAppsBookAnalysisFragmentDevAppsDevApps.newInstance(barcodeAnalysis),
            devAppsBarcodeAnalysis = barcodeAnalysis
        )
    }

    private fun configureUnknownProductAnalysisView(
        barcodeAnalysis: DevAppsUnknownProductDevAppsBarcodeAnalysis
    ) {
        configureContentFragment(
            fragment = DevAppsProductAnalysisFragmentDevApps.newInstance(barcodeAnalysis),
            devAppsBarcodeAnalysis = barcodeAnalysis
        )
    }

    private fun configureDefaultBarcodeAnalysisView(
        barcodeAnalysis: DevAppsDefaultDevAppsBarcodeAnalysis
    ) = configureContentFragment(
        fragment = DevAppsDefaultDevAppsBarcodeAnalysisFragment.newInstance(barcodeAnalysis),
        devAppsBarcodeAnalysis = barcodeAnalysis
    )

    private fun configureContentFragment(fragment: Fragment, devAppsBarcodeAnalysis: DevAppsBarcodeAnalysis) {
        replaceFragment(
            containerViewId = viewBinding.activityBarcodeAnalysisContent.id,
            fragment = fragment
        )

        // Change le texte de la toolbar
        changeToolbarText(devAppsBarcodeAnalysis.barcode)
    }

    // ---- Utils ----

    private fun isInternetPermissionGranted(): Boolean {
        val permission: Int = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun determineAPIRemote(barcodeType: BarcodeType): DevAppsRemoteAPI =
        if(barcodeType == BarcodeType.UNKNOWN_PRODUCT) {
            when(devAppsSettingsManager.apiChoose) {
                getString(R.string.preferences_entry_value_food) -> DevAppsRemoteAPI.OPEN_FOOD_FACTS
                getString(R.string.preferences_entry_value_cosmetic) -> DevAppsRemoteAPI.OPEN_BEAUTY_FACTS
                getString(R.string.preferences_entry_value_pet_food) -> DevAppsRemoteAPI.OPEN_PET_FOOD_FACTS
                getString(R.string.preferences_entry_value_musicbrainz) -> DevAppsRemoteAPI.MUSICBRAINZ
                else -> DevAppsRemoteAPI.NONE
            }
        } else {
            when(barcodeType) {
                BarcodeType.FOOD -> DevAppsRemoteAPI.OPEN_FOOD_FACTS
                BarcodeType.BEAUTY -> DevAppsRemoteAPI.OPEN_BEAUTY_FACTS
                BarcodeType.PET_FOOD -> DevAppsRemoteAPI.OPEN_PET_FOOD_FACTS
                BarcodeType.MUSIC -> DevAppsRemoteAPI.MUSICBRAINZ
                BarcodeType.BOOK -> DevAppsRemoteAPI.OPEN_LIBRARY
                else -> DevAppsRemoteAPI.NONE
            }
        }

    // ---- UI ----

    private fun changeToolbarText(barcode: Barcode) {
        val tabText = getString(barcode.getBarcodeType().stringResource)
        supportActionBar?.title = tabText
    }

    // ---- Database Update ----

    private fun updateTypeIntoDatabase(devAppsBarcodeAnalysis: DevAppsBarcodeAnalysis) {
        val productName: String? = when (devAppsBarcodeAnalysis) {
            is DevAppsFoodDevAppsBarcodeAnalysis -> devAppsBarcodeAnalysis.name
            is DevAppsBookDevAppsBarcodeAnalysis -> devAppsBarcodeAnalysis.title
            is DevAppsMusicDevAppsBarcodeAnalysis -> devAppsBarcodeAnalysis.album?.let { album ->
                devAppsBarcodeAnalysis.artists?.convertToString()?.let { artist ->
                    "$album - $artist"
                } ?: album
            }

            else -> null
        }

        val barcode = devAppsBarcodeAnalysis.barcode
        val newBarcodeType = devAppsBarcodeAnalysis.source.barcodeType
        if (!productName.isNullOrBlank()) {
            if (barcode.name != productName || barcode.getBarcodeType() != newBarcodeType)
                devAppsDatabaseBarcodeViewModel.updateTypeAndName(
                    barcode.scanDate,
                    newBarcodeType,
                    productName.trim()
                )
        } else {
            if (barcode.getBarcodeType() != newBarcodeType)
                devAppsDatabaseBarcodeViewModel.updateType(barcode.scanDate, newBarcodeType)
        }

        barcode.name = productName ?: ""
        barcode.type = newBarcodeType.name
    }

    /**
     * Met à jour le contenu du code-barres.
     */
    fun updateBarcodeContents(barcode: Barcode, newContents: String) {
        if(barcode.contents == newContents)
            return

        barcode.contents = newContents
        barcode.type = get<BarcodeType> { parametersOf(barcode) }.name
        barcode.name = ""
        barcode.updateCountry()

        devAppsDatabaseBarcodeViewModel.update(
            date = barcode.scanDate,
            contents = barcode.contents,
            barcodeType = barcode.getBarcodeType(),
            name = barcode.name
        )

        changeToolbarText(barcode)
        when {
            barcode.is1DProductBarcodeFormat -> {
                if(devAppsSettingsManager.useSearchOnApi) {
                    fetchProductFromRemoteAPI(barcode)
                } else {
                    fetchProductFromRemoteAPI(barcode, DevAppsRemoteAPI.NONE)
                }
            }
            else -> configureDefaultBarcodeAnalysisView(DevAppsDefaultDevAppsBarcodeAnalysis(barcode))
        }
    }
}