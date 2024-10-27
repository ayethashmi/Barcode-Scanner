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
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.common.extensions.serializable
import com.devappspros.barcodescanner.common.extensions.setImageColorFromAttrRes
import com.devappspros.barcodescanner.databinding.FragmentFoodAnalysisNutrientLevelBinding
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNutrient
import org.koin.android.ext.android.get

/**
 * A simple [Fragment] subclass.
 */
class DevAppsFoodAnalysisNutrientLevelFragment : Fragment() {

    private var devAppsNutrient: DevAppsNutrient? = null

    private var _binding: FragmentFoodAnalysisNutrientLevelBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            devAppsNutrient = it.serializable(NUTRIENT_KEY, DevAppsNutrient::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodAnalysisNutrientLevelBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureSubView(devAppsNutrient)
    }

    private fun configureSubView(devAppsNutrient: DevAppsNutrient?) {

        if(devAppsNutrient != null) {
            // Entitled
            val entitled: String = getString(devAppsNutrient.entitled.stringResource)
            viewBinding.fragmentFoodAnalysisNutrientLevelTitleTextView.text = entitled

            // Subtitle
            val subtitle: String = getString(devAppsNutrient.getQuantityValue().stringResource)
            viewBinding.fragmentFoodAnalysisNutrientLevelSubtitleTextView.text = subtitle

            // Weight
            val weight: String = devAppsNutrient.values.getValue100gString()
            viewBinding.fragmentFoodAnalysisNutrientLevelQuantityTextView.text = weight

            // Indicator Image
            @AttrRes val colorRes: Int = devAppsNutrient.getQuantityValue().colorResource
            viewBinding.fragmentFoodAnalysisNutrientLevelIndicatorImageView.setImageColorFromAttrRes(colorRes)

            // GraphView
            configureGraphView(devAppsNutrient)
        } else {
            viewBinding.root.visibility = View.GONE
        }
    }

    private fun configureGraphView(devAppsNutrient: DevAppsNutrient) {

        val currentQuantity: Float? = devAppsNutrient.values.value100g?.toFloat()
        val lowQuantity: Float? = devAppsNutrient.getLowQuantity()
        val highQuantity: Float? = devAppsNutrient.getHighQuantity()

        if(currentQuantity!=null && lowQuantity!=null && highQuantity!=null) {

            viewBinding.fragmentFoodAnalysisNutrientLevelHorizontalGraphView.setValues(
                guidePosition = currentQuantity,
                low = lowQuantity,
                high = highQuantity
            )
        } else {
            viewBinding.fragmentFoodAnalysisNutrientLevelHorizontalGraphView.visibility = View.GONE
        }
    }

    companion object {
        private const val NUTRIENT_KEY = "nutrientKey"

        @JvmStatic
        fun newInstance(devAppsNutrient: DevAppsNutrient) =
            DevAppsFoodAnalysisNutrientLevelFragment().apply {
                arguments = get<Bundle>().apply {
                    putSerializable(NUTRIENT_KEY, devAppsNutrient)
                }
            }
    }
}