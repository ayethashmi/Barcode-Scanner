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
import com.devappspros.barcodescanner.databinding.ActivityAboutBddBinding
import com.devappspros.barcodescanner.presentation.views.recyclerView.aboutBdd.BddItemAdapter

class DevAppsDevAppsAboutBddActivityDevApps : DevAppsBaseActivity() {

    private val viewBinding: ActivityAboutBddBinding by lazy {
        ActivityAboutBddBinding.inflate(layoutInflater)
    }
    override val rootView: View get() = viewBinding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(viewBinding.activityAboutBddActivityLayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)// On affiche l'icone "retour"

        configureRecyclerView()

        setContentView(rootView)
    }

    private fun configureRecyclerView(){

        val recyclerView = viewBinding.activityAboutBddRecyclerView

        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)
        val adapter = BddItemAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(dividerItemDecoration)
    }
}