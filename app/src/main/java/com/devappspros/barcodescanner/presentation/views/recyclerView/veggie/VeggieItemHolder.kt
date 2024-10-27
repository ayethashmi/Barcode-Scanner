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

package com.devappspros.barcodescanner.presentation.views.recyclerView.veggie

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.devappspros.barcodescanner.common.extensions.firstCharacterIntoCapital
import com.devappspros.barcodescanner.common.extensions.setTextColorFromAttrRes
import com.devappspros.barcodescanner.databinding.RecyclerViewItemVeggieBinding
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsVeggieIngredientAnalysis

class VeggieItemHolder(private val viewBinding: RecyclerViewItemVeggieBinding): RecyclerView.ViewHolder(viewBinding.root) {

    private val context: Context = itemView.context

    fun update(devAppsVeggieIngredientAnalysis: DevAppsVeggieIngredientAnalysis){
        viewBinding.recyclerViewItemVeggieIngredientTextView.text = (devAppsVeggieIngredientAnalysis.ingredientName ?: "?").trim().firstCharacterIntoCapital()

        val veganStatus = devAppsVeggieIngredientAnalysis.devAppsVeganStatus
        val vegetarianStatus = devAppsVeggieIngredientAnalysis.devAppsVegetarianStatus

        viewBinding.recyclerViewItemVeggieVeganTextView.text = context.getText(veganStatus.stringResource)
        viewBinding.recyclerViewItemVeggieVeganTextView.setTextColorFromAttrRes(veganStatus.colorResource)
        viewBinding.recyclerViewItemVeggieVegetarianTextView.text = context.getText(vegetarianStatus.stringResource)
        viewBinding.recyclerViewItemVeggieVegetarianTextView.setTextColorFromAttrRes(vegetarianStatus.colorResource)
    }
}