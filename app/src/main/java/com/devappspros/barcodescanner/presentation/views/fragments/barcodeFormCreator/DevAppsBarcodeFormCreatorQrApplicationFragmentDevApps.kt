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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.databinding.FragmentBarcodeFormCreatorQrApplicationBinding
import com.devappspros.barcodescanner.presentation.customView.DevAppsMarginItemDecoration
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsInstalledAppsViewModel
import com.devappspros.barcodescanner.presentation.views.recyclerView.applications.ApplicationsItem
import com.devappspros.barcodescanner.presentation.views.recyclerView.applications.ApplicationsItemAdapter
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DevAppsBarcodeFormCreatorQrApplicationFragmentDevApps : DevAppsAbstractBarcodeFormCreatorQrFragment(), ApplicationsItemAdapter.OnApplicationItemListener {

    companion object {
        private const val PREFIX = "market://details?id="
    }

    private val viewModel by activityViewModel<DevAppsInstalledAppsViewModel>()

    private var _binding: FragmentBarcodeFormCreatorQrApplicationBinding? = null
    private val viewBinding get() = _binding!!

    private val adapter = ApplicationsItemAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBarcodeFormCreatorQrApplicationBinding.inflate(inflater, container, false)

        /*viewBinding.fragmentBarcodeFormCreatorQrApplicationRecyclerView.visibility = View.GONE
        viewBinding.fragmentBarcodeFormCreatorQrApplicationProgressBar.visibility = View.VISIBLE*/

        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView()

        viewModel.installedApps.observe(viewLifecycleOwner) {
            viewBinding.fragmentBarcodeFormCreatorQrApplicationProgressBar.visibility = View.GONE
            adapter.updateData(it)
        }
    }

    private fun configureRecyclerView() {
        val recyclerView = viewBinding.fragmentBarcodeFormCreatorQrApplicationRecyclerView

        val layoutManager = LinearLayoutManager(requireContext())
        val decoration = DevAppsMarginItemDecoration(resources.getDimensionPixelSize(R.dimen.normal_margin))

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(decoration)

        //recyclerView.visibility = View.VISIBLE
    }

    private var barcodeContents = ""
    override fun getBarcodeTextFromForm(): String = barcodeContents

    override fun onItemClick(view: View?, item: ApplicationsItem) {
        barcodeContents = "${PREFIX}${item.pkg}"
        generateBarcode()
    }
}