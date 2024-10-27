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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions

import android.view.View
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeType
import com.devappspros.barcodescanner.presentation.views.recyclerView.actionButton.ActionItem

class DevAppsBeautyActionsFragmentDevAppsDevApps: DevAppsAbstractActionsFragmentDevApps() {
    override fun configureActionItems(barcode: Barcode) {
        if(barcode.getBarcodeType() == BarcodeType.BEAUTY) {
            addActionItem(configureBeautyActions(barcode))
        } else {
            addActionItem(configureSearchOnWebActionItem(barcode))
        }
        addActionItem(configureShareTextActionItem(barcode))
        addActionItem(configureCopyTextActionItem(barcode))
        addActionItem(configureModifyBarcodeActionItem(barcode))
    }

    private fun configureBeautyActions(barcode: Barcode): ActionItem {
        return ActionItem(
            textRes = R.string.action_web_search_label,
            imageRes = R.drawable.baseline_search_24,
            listener = showUrlsAlertDialog(barcode.contents)
        )
    }

    private fun showUrlsAlertDialog(contents: String): ActionItem.OnActionItemListener =
        object : ActionItem.OnActionItemListener {
            override fun onItemClick(view: View?) {
                val openBeautyFactsUrl = getString(R.string.search_engine_open_beauty_facts_product_url, contents)

                val items = arrayOf<Triple<String, Int, ActionItem.OnActionItemListener>>(
                    Triple(getString(R.string.action_web_search_label), R.drawable.baseline_web_24, openContentsWithSearchEngine(contents)),
                    Triple(getString(R.string.action_product_search_label, getString(R.string.open_beauty_facts_label)), R.drawable.baseline_face_24, openUrl(openBeautyFactsUrl))
                )

                createAlertDialog(requireContext(), getString(R.string.search_label), items).show()
            }
        }
}