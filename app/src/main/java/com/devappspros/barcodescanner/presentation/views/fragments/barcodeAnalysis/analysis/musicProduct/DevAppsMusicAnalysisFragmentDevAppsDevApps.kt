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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.musicProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.convertToString
import com.devappspros.barcodescanner.common.extensions.fixAnimateLayoutChangesInNestedScroll
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.databinding.FragmentMusicAnalysisBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsMusicDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.product.musicProduct.DevAppsAlbumTrack
import com.devappspros.barcodescanner.domain.library.DevAppsDateConverter
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.about.DevAppsDevAppsBarcodeAboutOverviewFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.DevAppsApiAnalysisFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.templates.ProductOverviewFragment
import com.devappspros.barcodescanner.presentation.views.recyclerView.musicAlbumTracks.MusicAlbumTracksAdapter
import org.koin.android.ext.android.get

/**
 * Vue affichant les informations de MusicBrainz.
 */
class DevAppsMusicAnalysisFragmentDevAppsDevApps : DevAppsApiAnalysisFragmentDevApps<DevAppsMusicDevAppsBarcodeAnalysis>() {

    private var _binding: FragmentMusicAnalysisBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMusicAnalysisBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsMusicDevAppsBarcodeAnalysis) {
        super.start(analysis)
        viewBinding.fragmentMusicAnalysisOuterView.fixAnimateLayoutChangesInNestedScroll()
        configureProductOverviewFragment(analysis)
        configureTracksView(analysis.devAppsAlbumTracks, analysis.artists?.convertToString())
        configureBarcodeAboutOverviewFragment()
    }

    private fun configureProductOverviewFragment(product: DevAppsMusicDevAppsBarcodeAnalysis) {
        val fragment = ProductOverviewFragment.newInstance(
            imageUrl = product.coverUrl,
            title = product.album ?: getString(R.string.bar_code_type_unknown_music_album_title),
            subtitle1 = product.artists?.convertToString(),
            subtitle2 = get<DevAppsDateConverter>().convertDateToLocalizedFormat(requireContext(), product.date),
            subtitle3 = product.trackCount?.let { getString(R.string.music_product_tracks_number, "$it") }
        )
        applyFragment(viewBinding.fragmentMusicAnalysisOverviewLayout.id, fragment)
    }

    private fun configureTracksView(tracks: List<DevAppsAlbumTrack>?, artists: String?) {
        if(tracks.isNullOrEmpty()) {
            viewBinding.fragmentMusicAnalysisTracksCardView.visibility = View.GONE
        } else {
            val linearLayoutManager = LinearLayoutManager(requireActivity())
            viewBinding.fragmentMusicAnalysisTracksRecyclerView.apply {
                this.adapter = MusicAlbumTracksAdapter(tracks, artists)
                this.layoutManager = linearLayoutManager
                this.addItemDecoration(DividerItemDecoration(requireActivity(), linearLayoutManager.orientation))
            }
            viewBinding.fragmentMusicAnalysisTracksCardView.visibility = View.VISIBLE
        }
    }

    private fun configureBarcodeAboutOverviewFragment() = applyFragment(
        containerViewId = viewBinding.fragmentMusicAnalysisBarcodeAboutOverviewFrameLayout.id,
        fragmentClass = DevAppsDevAppsBarcodeAboutOverviewFragment::class,
        args = arguments
    )

    companion object {
        fun newInstance(musicProduct: DevAppsMusicDevAppsBarcodeAnalysis) = DevAppsMusicAnalysisFragmentDevAppsDevApps().apply {
            arguments = get<Bundle>().apply {
                putSerializable(BARCODE_ANALYSIS_KEY, musicProduct)
            }
        }
    }
}