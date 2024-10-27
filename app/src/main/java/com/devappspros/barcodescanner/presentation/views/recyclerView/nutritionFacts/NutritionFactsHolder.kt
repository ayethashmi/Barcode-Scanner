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

package com.devappspros.barcodescanner.presentation.views.recyclerView.nutritionFacts

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.setBackground
import com.devappspros.barcodescanner.databinding.RecyclerViewItemNutritionFactsBinding
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNutrient
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNutritionFactsEnum

class NutritionFactsHolder(private val viewBinding: RecyclerViewItemNutritionFactsBinding): RecyclerView.ViewHolder(viewBinding.root) {

    private val context = itemView.context

    fun updateItem(devAppsNutrient: DevAppsNutrient, showServing: Boolean) {
        val entitledTextView = viewBinding.recyclerViewItemNutritionFactsEntitledTextView

        // Pour les sous-nutriments
        if(devAppsNutrient.entitled == DevAppsNutritionFactsEnum.SATURATED_FAT ||
            devAppsNutrient.entitled == DevAppsNutritionFactsEnum.SUGARS ||
            devAppsNutrient.entitled == DevAppsNutritionFactsEnum.STARCH ||
            devAppsNutrient.entitled == DevAppsNutritionFactsEnum.SODIUM){

            viewBinding.root.setBackground(R.attr.colorSurfaceContainerLow)
            val leftPadding = context.resources.getDimension(R.dimen.large_margin).toInt()
            entitledTextView.setPadding(
                leftPadding,
                entitledTextView.paddingTop,
                entitledTextView.paddingRight,
                entitledTextView.paddingBottom
            )
        } else {
            viewBinding.root.setBackground(R.attr.colorSurfaceContainer)
        }

        entitledTextView.text = context.getString(devAppsNutrient.entitled.stringResource)

        viewBinding.recyclerViewItemNutritionFacts100gValueTextView.text = devAppsNutrient.values.getValue100gString()

        if (showServing)
            viewBinding.recyclerViewItemNutritionFactsServingValueTextView.text = devAppsNutrient.values.getValueServingString()
        else
            viewBinding.recyclerViewItemNutritionFactsServingValueTextView.visibility = View.GONE
    }
}