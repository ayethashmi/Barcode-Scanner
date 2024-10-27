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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.unknownProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.databinding.FragmentProductAnalysisErrorBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsUnknownProductDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.library.DevAppsInternetChecker
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 */
class DevAppsProductAnalysisErrorFragmentDevApps: DevAppsBarcodeAnalysisFragment<DevAppsUnknownProductDevAppsBarcodeAnalysis>() {

    private var _binding: FragmentProductAnalysisErrorBinding? = null
    private val viewBinding get() = _binding!!

    private val devAppsInternetChecker: DevAppsInternetChecker by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductAnalysisErrorBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsUnknownProductDevAppsBarcodeAnalysis) {
        configureInformationTextViewError()
        configureMessageTextViewError(analysis.message)
    }

    private fun configureInformationTextViewError() {
        val isConnected = devAppsInternetChecker.isInternetAvailable()

        val msg = if(!isConnected)
            getString(R.string.scan_error_internet_information_label)
        else getString(R.string.scan_error_information_label)

        viewBinding.fragmentProductAnalysisErrorInformationTextView.text = msg
    }

    private fun configureMessageTextViewError(message: String?) = displayText(
        textView = viewBinding.fragmentProductAnalysisErrorMessageTextView,
        layout = viewBinding.fragmentProductAnalysisErrorMessageLayout,
        text = message
    )
}