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
import com.devappspros.barcodescanner.presentation.intent.createAddAgendaIntent
import com.devappspros.barcodescanner.presentation.views.recyclerView.actionButton.ActionItem
import com.google.zxing.client.result.CalendarParsedResult
import com.google.zxing.client.result.ParsedResult

class DevAppsAgendaActionsFragmentDevAppsDevApps: DevAppsAbstractParsedResultActionsFragmentDevApps() {

    override fun configureActionItems(barcode: Barcode, parsedResult: ParsedResult) {
        if(parsedResult is CalendarParsedResult) {
            addActionItem(configureAddToAgendaActionItem(parsedResult))
        }
        addActionItem(configureSearchOnWebActionItem(barcode))
        addActionItem(configureShareTextActionItem(barcode))
        addActionItem(configureCopyTextActionItem(barcode))
        addActionItem(configureModifyBarcodeActionItem(barcode))
    }

    private fun configureAddToAgendaActionItem(parsedResult: CalendarParsedResult): ActionItem {
        return ActionItem(
            textRes = R.string.action_add_to_calendar,
            imageRes = R.drawable.baseline_event_note_24,
            listener = addToAgenda(parsedResult)
        )
    }

    private fun addToAgenda(parsedResult: CalendarParsedResult): ActionItem.OnActionItemListener = object : ActionItem.OnActionItemListener {
        override fun onItemClick(view: View?) {
            val intent = createAddAgendaIntent(parsedResult)
            mStartActivity(intent)
        }
    }
}