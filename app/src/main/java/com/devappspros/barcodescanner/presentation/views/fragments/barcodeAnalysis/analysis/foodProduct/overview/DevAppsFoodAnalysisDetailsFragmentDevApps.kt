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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.foodProduct.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.convertToString
import com.devappspros.barcodescanner.common.extensions.fixAnimateLayoutChangesInNestedScroll
import com.devappspros.barcodescanner.databinding.FragmentFoodAnalysisDetailsBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.dependencies.Country
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsExternalFileViewModel
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * A simple [Fragment] subclass.
 */
class DevAppsFoodAnalysisDetailsFragmentDevApps: DevAppsBarcodeAnalysisFragment<DevAppsFoodDevAppsBarcodeAnalysis>() {

    private val viewModel: DevAppsExternalFileViewModel by activityViewModel()

    private var _binding: FragmentFoodAnalysisDetailsBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodAnalysisDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsFoodDevAppsBarcodeAnalysis) {
        viewBinding.root.fixAnimateLayoutChangesInNestedScroll()
        configureCategories(analysis)
        configurePackaging(analysis)
        configureStores(analysis)

        observeCountries(analysis)
    }

    private fun configureCategories(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) = configureExpandableCardViewFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisDetailsCategoriesFrameLayout,
        title = getString(R.string.categories_label),
        contents = foodProduct.categories
    )

    private fun configurePackaging(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) = configureExpandableCardViewFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisDetailsPackagingFrameLayout,
        title = getString(R.string.packaging_label),
        contents = foodProduct.packaging
    )

    private fun configureStores(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) = configureExpandableCardViewFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisDetailsStoresFrameLayout,
        title = getString(R.string.stores_label),
        contents = foodProduct.stores
    )

    private fun configureOriginsCountries(countries: String?) = configureExpandableCardViewFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisDetailsOriginsCountriesFrameLayout,
        title = getString(R.string.origins_label),
        contents = countries
    )

    private fun configureSalesCountries(countries: String?) = configureExpandableCardViewFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisDetailsSalesCountriesFrameLayout,
        title = getString(R.string.countries_label),
        contents = countries
    )

    private fun observeCountries(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis){

        val countriesTags = foodProduct.countriesTagList

        if(!countriesTags.isNullOrEmpty()) {

            viewModel.obtainCountriesList(countriesTags).observe(viewLifecycleOwner) {

                val salesCountriesStr: String?
                val originsCountriesStr: String?
                if (it.isNotEmpty()) {
                    salesCountriesStr =
                        obtainCountriesString(foodProduct.salesCountriesTagsList, it)
                    originsCountriesStr =
                        obtainCountriesString(foodProduct.originsCountriesTagsList, it)
                } else {
                    salesCountriesStr = foodProduct.salesCountriesTagsList?.convertToString()
                    originsCountriesStr = foodProduct.originsCountriesTagsList?.convertToString()
                }

                configureSalesCountries(salesCountriesStr)
                configureOriginsCountries(originsCountriesStr)
            }
        }else{
            viewBinding.fragmentFoodAnalysisDetailsOriginsCountriesFrameLayout.visibility = View.GONE
            viewBinding.fragmentFoodAnalysisDetailsSalesCountriesFrameLayout.visibility = View.GONE
        }
    }

    private fun obtainCountriesString(countriesTagsList: List<String>?, countriesList: List<Country>): String?{
        return if (!countriesTagsList.isNullOrEmpty()) {
            val countries = mutableListOf<String>()
            for (country in countriesList) {
                if(countriesTagsList.contains(country.tag)){
                    countries.add(country.name)
                }
            }

            countries.sort()
            countries.convertToString()
        } else null
    }
}