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

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.databinding.FragmentBarcodeMatrixUriBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsBarcodeAnalysis
import com.google.zxing.client.result.ParsedResult
import com.google.zxing.client.result.ParsedResultType
import com.google.zxing.client.result.URIParsedResult

/**
 * A simple [Fragment] subclass.
 */
class DevAppsDevAppsBarcodeMatrixUriFragmentDevApps: DevAppsAbstractDevAppsBarcodeMatrixFragment() {

    private var _binding: FragmentBarcodeMatrixUriBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBarcodeMatrixUriBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(product: DevAppsBarcodeAnalysis, parsedResult: ParsedResult) {
        if(parsedResult is URIParsedResult && parsedResult.type == ParsedResultType.URI) {
            val uri = parsedResult.uri
            viewBinding.fragmentBarcodeMatrixUriUrlLayout.setContentsText(uri)
            configureIsPossiblyMaliciousURI(parsedResult.isPossiblyMaliciousURI)
            if(uri.startsWith("upi")) {
                applyFragment(
                    containerViewId = viewBinding.fragmentBarcodeMatrixUriParsedLayout.id,
                    fragment = DevAppsBarcodeMatrixUpiParsedFragment.newInstance(uri)
                )
            } else {
                viewBinding.fragmentBarcodeMatrixUriParsedLayout.visibility = View.GONE
            }
        } else {
            viewBinding.root.visibility = View.GONE
        }
    }

    private fun configureIsPossiblyMaliciousURI(isPossiblyMaliciousURI: Boolean?) {
        if (isPossiblyMaliciousURI != true) {
            viewBinding.fragmentBarcodeMatrixUriMaliciousLayout.visibility = View.GONE
        }
    }
}