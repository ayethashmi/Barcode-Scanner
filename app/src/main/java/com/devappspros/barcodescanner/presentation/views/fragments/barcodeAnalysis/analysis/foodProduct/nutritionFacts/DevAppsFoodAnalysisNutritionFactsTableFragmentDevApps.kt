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
import androidx.recyclerview.widget.LinearLayoutManager
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.databinding.FragmentFoodAnalysisNutritionFactsTableBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import com.devappspros.barcodescanner.presentation.views.recyclerView.nutritionFacts.NutritionFactsAdapter

/**
 * A simple [Fragment] subclass.
 */
class DevAppsFoodAnalysisNutritionFactsTableFragmentDevApps : DevAppsBarcodeAnalysisFragment<DevAppsFoodDevAppsBarcodeAnalysis>() {

    private var _binding: FragmentFoodAnalysisNutritionFactsTableBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodAnalysisNutritionFactsTableBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsFoodDevAppsBarcodeAnalysis) {
        configureHeaderRowTable(analysis)
        configureRecyclerView(analysis)
    }

    private fun configureHeaderRowTable(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis){
        // Entitled
        viewBinding.fragmentFoodAnalysisNutritionFactsTableEntitled100TextView.text =
            getString(R.string.off_per_100_label, foodProduct.unit)

        if(!foodProduct.containsServingValues){
            viewBinding.fragmentFoodAnalysisNutritionFactsTableEntitledServingTextView.visibility = View.GONE
        }
        else {
            viewBinding.fragmentFoodAnalysisNutritionFactsTableEntitledServingTextView.text =
                if(foodProduct.servingQuantity==null)
                    getString(R.string.off_per_serving_no_quantity_label)
                else
                    getString(R.string.off_per_serving_label, foodProduct.servingQuantity.toString(), foodProduct.unit)
        }

    }

    private fun configureRecyclerView(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis){
        viewBinding.fragmentFoodAnalysisNutritionFactsTableRecyclerView.adapter = NutritionFactsAdapter(foodProduct.nutrientsList, foodProduct.containsServingValues)
        viewBinding.fragmentFoodAnalysisNutritionFactsTableRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.fragmentFoodAnalysisNutritionFactsTableRecyclerView.suppressLayout(true)
    }
}