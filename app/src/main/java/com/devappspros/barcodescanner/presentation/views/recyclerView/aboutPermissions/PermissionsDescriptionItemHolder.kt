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

package com.devappspros.barcodescanner.presentation.views.recyclerView.aboutPermissions

import androidx.recyclerview.widget.RecyclerView
import com.devappspros.barcodescanner.databinding.RecyclerViewItemAboutBinding

class PermissionsDescriptionItemHolder(private val viewBinding: RecyclerViewItemAboutBinding)
    : RecyclerView.ViewHolder(viewBinding.root) {

    private val context = itemView.context

    fun updateItem(permissionsDescription: PermissionsDescription) {
        viewBinding.recyclerViewItemAboutTitleTextView.text = context.getString(permissionsDescription.nameResource)
        viewBinding.recyclerViewItemAboutDescriptionTextView.text = context.getString(permissionsDescription.descriptionResource)
    }
}