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

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.devappspros.barcodescanner.common.extensions.serializable
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.databinding.ActivityVeggieBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsVeggieIngredientAnalysis
import com.devappspros.barcodescanner.presentation.views.recyclerView.veggie.VeggieItemAdapter

class VeggieActivityDevApps : DevAppsBaseActivity() {

    private val viewBinding: ActivityVeggieBinding by lazy { ActivityVeggieBinding.inflate(layoutInflater) }
    override val rootView: View get() = viewBinding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureToolbar()

        val foodProduct: DevAppsFoodDevAppsBarcodeAnalysis? = intent.serializable(BARCODE_ANALYSIS_KEY, DevAppsFoodDevAppsBarcodeAnalysis::class.java)

        val veggieIngredientsList = foodProduct?.veggieIngredientList
        if(veggieIngredientsList.isNullOrEmpty()) {
            viewBinding.activityVeggieNestedScrollView.visibility = View.GONE
            viewBinding.activityVeggieNoIngredientsTextView.visibility = View.VISIBLE
        } else {
            configureRecyclerView(veggieIngredientsList)
            viewBinding.activityVeggieNestedScrollView.visibility = View.VISIBLE
            viewBinding.activityVeggieNoIngredientsTextView.visibility = View.GONE
        }

        setContentView(rootView)
    }

    private fun configureToolbar() {
        setSupportActionBar(viewBinding.activityVeggieActivityLayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)// On affiche l'icone "retour"
    }

    private fun configureRecyclerView(veggieIngredientList: List<DevAppsVeggieIngredientAnalysis>) {
        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)
        val adapter = VeggieItemAdapter(veggieIngredientList)
        viewBinding.activityVeggieRecyclerView.let { recyclerView ->
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.adapter = adapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
    }
}