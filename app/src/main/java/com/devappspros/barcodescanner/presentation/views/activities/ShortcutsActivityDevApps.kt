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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.databinding.ActivityShortcutsBinding
import com.devappspros.barcodescanner.presentation.customView.DevAppsCustomItemTouchHelperCallback
import com.devappspros.barcodescanner.presentation.customView.DevAppsMarginItemDecoration
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDynamicShortcutViewModel
import com.devappspros.barcodescanner.presentation.views.recyclerView.shortcuts.ShortcutItemAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShortcutsActivityDevApps : DevAppsBaseActivity(), DevAppsCustomItemTouchHelperCallback.ItemTouchHelperListener {

    private val viewBinding: ActivityShortcutsBinding by lazy { ActivityShortcutsBinding.inflate(layoutInflater) }
    override val rootView: View get() = viewBinding.root
    private val shortcutViewModel: DevAppsDynamicShortcutViewModel by viewModel()

    private val adapter: ShortcutItemAdapter = ShortcutItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(viewBinding.activityShortcutsActivityLayout.toolbar)
        shortcutViewModel.createShortcuts()
        configureRecyclerView()
        setContentView(rootView)
    }

    private fun configureRecyclerView() {
        val recyclerView = viewBinding.activityShortcutsRecyclerView

        val layoutManager = LinearLayoutManager(this)
        val decoration = DevAppsMarginItemDecoration(resources.getDimensionPixelSize(R.dimen.normal_margin))

        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(decoration)

        val itemTouchHelperCallback = DevAppsCustomItemTouchHelperCallback(this, ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        adapter.updateData(shortcutViewModel.getShortcuts())
    }

    override fun getForegroundView(viewHolder: RecyclerView.ViewHolder?): View? {
        return viewHolder?.itemView
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {}

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.bindingAdapterPosition
        val toPosition = target.bindingAdapterPosition
        adapter.moveItem(fromPosition, toPosition)
        shortcutViewModel.updateShortcuts(adapter.items)
        return true
    }
}