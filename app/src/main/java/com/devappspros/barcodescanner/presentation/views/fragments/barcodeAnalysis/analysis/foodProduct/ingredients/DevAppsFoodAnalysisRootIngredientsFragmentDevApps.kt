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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.foodProduct.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.fixAnimateLayoutChangesInNestedScroll
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.databinding.FragmentFoodAnalysisRootIngredientsBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import org.koin.android.ext.android.get

/**
 * A simple [Fragment] subclass.
 */
class DevAppsFoodAnalysisRootIngredientsFragmentDevApps : DevAppsBarcodeAnalysisFragment<DevAppsFoodDevAppsBarcodeAnalysis>() {

    private var _binding: FragmentFoodAnalysisRootIngredientsBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding=FragmentFoodAnalysisRootIngredientsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsFoodDevAppsBarcodeAnalysis) {

        viewBinding.fragmentFoodAnalysisRootIngredientsOuterView.fixAnimateLayoutChangesInNestedScroll()
        configureIngredientsView(analysis)
        configureAdditivesView(analysis)
    }

    private fun configureIngredientsView(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) {
        if(foodProduct.ingredients != null && foodProduct.ingredients != "") {
            applyFragment(R.id.fragment_food_analysis_root_ingredients_ingredients_frame_layout, DevAppsFoodAnalysisIngredientsFragmentDevApps::class, arguments)
            viewBinding.fragmentFoodAnalysisRootIngredientsNoInformationTextView.visibility=View.GONE
        }
    }

    private fun configureAdditivesView(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) {
        if (foodProduct.additivesTagsList != null && foodProduct.additivesTagsList.isNotEmpty()) {
            applyFragment(R.id.fragment_food_analysis_root_ingredients_additives_frame_layout, DevAppsFoodAnalysisAdditivesFragmentDevApps::class, arguments)
            viewBinding.fragmentFoodAnalysisRootIngredientsNoInformationTextView.visibility=View.GONE
        }
    }

    companion object {
        fun newInstance(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) = DevAppsFoodAnalysisRootIngredientsFragmentDevApps().apply {
            arguments = get<Bundle>().apply {
                putSerializable(BARCODE_ANALYSIS_KEY, foodProduct)
            }
        }
    }
}
