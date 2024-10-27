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
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.common.extensions.getColorInt
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.databinding.FragmentFoodAnalysisVeggieBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.presentation.intent.createStartActivityIntent
import com.devappspros.barcodescanner.presentation.views.activities.VeggieActivityDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment

/**
 * A simple [Fragment] subclass.
 */
class DevAppsFoodAnalysisVeggieFragmentDevApps: DevAppsBarcodeAnalysisFragment<DevAppsFoodDevAppsBarcodeAnalysis>() {

    private var _binding: FragmentFoodAnalysisVeggieBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodAnalysisVeggieBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsFoodDevAppsBarcodeAnalysis) {

        val onClickListener: View.OnClickListener = getVeggieOnClickListener(analysis)

        configureAnalysis(
            view = viewBinding.fragmentFoodAnalysisVeggieVegan.root,
            checkIconView = viewBinding.fragmentFoodAnalysisVeggieVegan.templateIngredientsAnalysisCheckIcon,
            textView = viewBinding.fragmentFoodAnalysisVeggieVegan.templateIngredientsAnalysisTextView,
            colorResource = analysis.devAppsVeganStatus.colorResource,
            stringResource = analysis.devAppsVeganStatus.stringResource,
            onClickListener = onClickListener
        )

        configureAnalysis(
            view = viewBinding.fragmentFoodAnalysisVeggieVegetarian.root,
            checkIconView = viewBinding.fragmentFoodAnalysisVeggieVegetarian.templateIngredientsAnalysisCheckIcon,
            textView = viewBinding.fragmentFoodAnalysisVeggieVegetarian.templateIngredientsAnalysisTextView,
            colorResource = analysis.devAppsVegetarianStatus.colorResource,
            stringResource = analysis.devAppsVegetarianStatus.stringResource,
            onClickListener = onClickListener
        )

        configureAnalysis(
            view = viewBinding.fragmentFoodAnalysisVeggiePalmOil.root,
            checkIconView = viewBinding.fragmentFoodAnalysisVeggiePalmOil.templateIngredientsAnalysisCheckIcon,
            textView = viewBinding.fragmentFoodAnalysisVeggiePalmOil.templateIngredientsAnalysisTextView,
            colorResource = analysis.devAppsPalmOilStatus.colorResource,
            stringResource = analysis.devAppsPalmOilStatus.stringResource
        )
        viewBinding.fragmentFoodAnalysisVeggiePalmOil.templateIngredientsAnalysisEndIcon.visibility = View.INVISIBLE
    }

    private fun configureAnalysis(
        view: View,
        checkIconView: ImageView,
        textView: TextView,
        @AttrRes colorResource: Int,
        @StringRes stringResource: Int,
        onClickListener: View.OnClickListener? = null
    ) {
        textView.setText(stringResource)
        checkIconView.setColorFilter(requireContext().getColorInt(colorResource))
        onClickListener?.let { view.setOnClickListener(it) }
    }

    private fun getVeggieOnClickListener(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) = View.OnClickListener {
        val intent = createStartActivityIntent(requireContext(), VeggieActivityDevApps::class).apply {
            putExtra(BARCODE_ANALYSIS_KEY, foodProduct)
        }
        startActivity(intent)
    }
}