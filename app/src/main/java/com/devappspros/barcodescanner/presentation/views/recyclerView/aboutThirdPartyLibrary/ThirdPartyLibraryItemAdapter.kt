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

package com.devappspros.barcodescanner.presentation.views.recyclerView.aboutThirdPartyLibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devappspros.barcodescanner.databinding.RecyclerViewItemAboutThirdPartyLibrariesBinding

class ThirdPartyLibraryItemAdapter: RecyclerView.Adapter<ThirdPartyLibraryItemHolder>() {

    private val thirdPartyLibraryArrays: Array<ThirdPartyLibrary> = ThirdPartyLibrary.entries.toTypedArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThirdPartyLibraryItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ThirdPartyLibraryItemHolder(
            RecyclerViewItemAboutThirdPartyLibrariesBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int = thirdPartyLibraryArrays.size

    override fun onBindViewHolder(holder: ThirdPartyLibraryItemHolder, position: Int) {
        holder.updateItem(thirdPartyLibraryArrays[position])
    }
}