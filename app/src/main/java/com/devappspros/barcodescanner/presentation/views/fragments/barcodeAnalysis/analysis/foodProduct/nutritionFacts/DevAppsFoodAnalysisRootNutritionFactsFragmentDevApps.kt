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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.foodProduct.nutritionFacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.fixAnimateLayoutChangesInNestedScroll
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.databinding.FragmentFoodAnalysisRootNutritionFactsBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNutrient
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNutritionFactsEnum
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import org.koin.android.ext.android.get

/**
 * A simple [Fragment] subclass.
 */
class DevAppsFoodAnalysisRootNutritionFactsFragmentDevApps : DevAppsBarcodeAnalysisFragment<DevAppsFoodDevAppsBarcodeAnalysis>() {

    private var _binding: FragmentFoodAnalysisRootNutritionFactsBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodAnalysisRootNutritionFactsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsFoodDevAppsBarcodeAnalysis) {
        viewBinding.fragmentFoodAnalysisRootNutritionFactsOuterView.fixAnimateLayoutChangesInNestedScroll()
        configureTable(analysis)
        configureNutrientLevel(analysis)
    }

    // ---- Table ----

    private fun configureTable(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis){
        viewBinding.fragmentFoodAnalysisRootNutritionFactsTableEntitledLayout.visibility = View.GONE
        if(foodProduct.contains100gValues || foodProduct.containsServingValues) {
            configureTableEntitled()
            configureOffNutritionFactsTableFragment()
            viewBinding.fragmentFoodAnalysisRootNutritionFactsNoInformationTextView.visibility = View.GONE
        }
    }

    private fun configureTableEntitled(){
        viewBinding.fragmentFoodAnalysisRootNutritionFactsTableEntitledLayout.visibility = View.VISIBLE
    }

    private fun configureOffNutritionFactsTableFragment() = applyFragment(
        containerViewId = R.id.fragment_food_analysis_root_nutrition_facts_table_frame_layout,
        fragmentClass = DevAppsFoodAnalysisNutritionFactsTableFragmentDevApps::class,
        args = arguments
    )

    // ---- Nutrient Level ----


    private fun configureNutrientLevel(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis){

        if(foodProduct.containsNutrientLevel){
            configureNutrientLevelEntitled()
            configureFoodProductNutrientLevelFragments(foodProduct.nutrientsList)
        }else{
            viewBinding.fragmentFoodAnalysisRootNutritionFactsNutrientLevelEntitledLayout.visibility = View.GONE
        }
    }

    private fun configureFoodProductNutrientLevelFragments(nutrientsList: List<DevAppsNutrient>){

        nutrientsList.forEach {
            when (it.entitled) {
                DevAppsNutritionFactsEnum.FAT -> configureFatFragment(it)
                DevAppsNutritionFactsEnum.SATURATED_FAT -> configureSaturatedFatFragment(it)
                DevAppsNutritionFactsEnum.SUGARS -> configureSugarsFragment(it)
                DevAppsNutritionFactsEnum.SALT -> configureSaltFragment(it)
                else -> {}
            }
        }
    }

    private fun configureFatFragment(devAppsNutrient: DevAppsNutrient) = applyFragment(
        containerViewId = viewBinding.fragmentFoodAnalysisRootNutritionFactsNutrientLevelFatLayout.id,
        fragment = DevAppsFoodAnalysisNutrientLevelFragment.newInstance(devAppsNutrient)
    )

    private fun configureSaturatedFatFragment(devAppsNutrient: DevAppsNutrient) = applyFragment(
        containerViewId = viewBinding.fragmentFoodAnalysisRootNutritionFactsNutrientLevelSaturatedFatLayout.id,
        fragment = DevAppsFoodAnalysisNutrientLevelFragment.newInstance(devAppsNutrient)
    )

    private fun configureSugarsFragment(devAppsNutrient: DevAppsNutrient) = applyFragment(
        containerViewId = viewBinding.fragmentFoodAnalysisRootNutritionFactsNutrientLevelSugarsLayout.id,
        fragment = DevAppsFoodAnalysisNutrientLevelFragment.newInstance(devAppsNutrient)
    )

    private fun configureSaltFragment(devAppsNutrient: DevAppsNutrient) = applyFragment(
        containerViewId = viewBinding.fragmentFoodAnalysisRootNutritionFactsNutrientLevelSaltLayout.id,
        fragment = DevAppsFoodAnalysisNutrientLevelFragment.newInstance(devAppsNutrient)
    )

    private fun configureNutrientLevelEntitled(){
        viewBinding.fragmentFoodAnalysisRootNutritionFactsNutrientLevelEntitledLayout.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) = DevAppsFoodAnalysisRootNutritionFactsFragmentDevApps().apply {
            arguments = get<Bundle>().apply {
                putSerializable(BARCODE_ANALYSIS_KEY, foodProduct)
            }
        }
    }
}
