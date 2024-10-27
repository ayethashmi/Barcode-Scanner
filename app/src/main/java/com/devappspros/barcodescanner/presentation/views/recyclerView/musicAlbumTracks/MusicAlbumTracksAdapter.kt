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

package com.devappspros.barcodescanner.presentation.views.recyclerView.musicAlbumTracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devappspros.barcodescanner.databinding.RecyclerViewItemMusicAlbumTrackBinding
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.DevAppsAlbumTrack

class MusicAlbumTracksAdapter(private val tracks: List<DevAppsAlbumTrack>,
                              private val artists: String?): RecyclerView.Adapter<MusicAlbumTracksHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAlbumTracksHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = RecyclerViewItemMusicAlbumTrackBinding.inflate(layoutInflater, parent, false)
        return MusicAlbumTracksHolder(viewBinding)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: MusicAlbumTracksHolder, position: Int) {
        holder.updateItem(tracks[position], artists)
    }
}