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
import com.devappspros.barcodescanner.common.extensions.convertToString
import com.devappspros.barcodescanner.common.extensions.fixAnimateLayoutChangesInNestedScroll
import com.devappspros.barcodescanner.common.extensions.polishText
import com.devappspros.barcodescanner.common.extensions.toHtmlSpanned
import com.devappspros.barcodescanner.databinding.FragmentFoodAnalysisIngredientsBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.dependencies.Allergen
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsExternalFileViewModel
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * A simple [Fragment] subclass.
 */
class DevAppsFoodAnalysisIngredientsFragmentDevApps : DevAppsBarcodeAnalysisFragment<DevAppsFoodDevAppsBarcodeAnalysis>() {

    private val viewModel: DevAppsExternalFileViewModel by activityViewModel()

    // ---- Views ----

    private var _binding: FragmentFoodAnalysisIngredientsBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding=FragmentFoodAnalysisIngredientsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsFoodDevAppsBarcodeAnalysis) {
        viewBinding.root.fixAnimateLayoutChangesInNestedScroll()
        configureIngredients(analysis)
        configureAllergensAndTracesView(analysis)
    }

    // ---- Ingredients ----

    private fun configureIngredients(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) {
        val ingredients = foodProduct.ingredients

        if (!ingredients.isNullOrBlank()) {

            val ingredientsWithAllergenBold = ingredients
                .replace("<span class=\"allergen\">", "<b>")
                .replace("</span>", "</b>")

            val ingredientsSpanned = "<span>$ingredientsWithAllergenBold</span>".toHtmlSpanned()
            configureIngredientsFragment(ingredientsSpanned)
        }
    }

    // ---- Allergens & Traces ----

    private fun configureAllergensAndTracesView(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis){
        if(foodProduct.allergensAndTracesTagList.isNullOrEmpty()){
            viewBinding.fragmentFoodAnalysisIngredientsAllergensFrameLayout.visibility = View.GONE
            viewBinding.fragmentFoodAnalysisIngredientsTracesFrameLayout.visibility = View.GONE
        }else{
            observeAllergensAndTraces(foodProduct.allergensAndTracesTagList, foodProduct.allergensTagsList, foodProduct.tracesTagsList)
        }
    }

    private fun observeAllergensAndTraces(allergensAndTracesTagsList: List<String>,
                                          allergensTagsList: List<String>?,
                                          tracesTagsList: List<String>?) {

        viewModel.obtainAllergensList(allergensAndTracesTagsList).observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val allergensList = mutableListOf<Allergen>()
                val tracesList = mutableListOf<Allergen>()

                // On sépare les allergens des traces
                for (allergenSchema in it) {

                    if (allergensTagsList?.contains(allergenSchema.tag) == true)
                        allergensList.add(allergenSchema)

                    if (tracesTagsList?.contains(allergenSchema.tag) == true)
                        tracesList.add(allergenSchema)
                }

                // Allergens
                if (allergensList.isNotEmpty()) {
                    val allergens: String = convertAllergensListToString(allergensList)
                    configureAllergensFragment(allergens)
                }

                // Traces
                if (tracesList.isNotEmpty()) {
                    val traces: String = convertAllergensListToString(tracesList)
                    configureTracesFragment(traces)
                }

            } else {
                configureAllergensFragment(allergensTagsList?.convertToString())
                configureTracesFragment(tracesTagsList?.convertToString())
            }
        }
    }

    private fun convertAllergensListToString(list: List<Allergen>): String {
        val strBuilder = StringBuilder()
        for (allergen in list) {

            strBuilder.append(allergen.name)

            if (list.last() != allergen)
                strBuilder.append(", ")
        }

        return strBuilder.toString().polishText()
    }

    // ---- Fragment Configuration ----

    private fun configureIngredientsFragment(ingredients: CharSequence?) = configureExpandableCardViewFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisIngredientsFrameLayout,
        title = getString(R.string.ingredients_label),
        contents = ingredients
    )

    private fun configureAllergensFragment(allergens: CharSequence?) = configureExpandableCardViewFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisIngredientsAllergensFrameLayout,
        title = getString(R.string.allergens_label),
        contents = allergens
    )

    private fun configureTracesFragment(traces: CharSequence?) = configureExpandableCardViewFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisIngredientsTracesFrameLayout,
        title = getString(R.string.traces_label),
        contents = traces
    )
}