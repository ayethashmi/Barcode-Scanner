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
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.databinding.ActivityBarcodeScanOnlyBinding
import com.devappspros.barcodescanner.presentation.views.fragments.main.DevAppsMainCameraXScannerFragment
import org.koin.android.ext.android.inject

class DevAppsBarcodeScanOnlyActivityDevApps : DevAppsBaseActivity() {

    private val devAppsMainCameraXScannerFragment: DevAppsMainCameraXScannerFragment by inject()

    private val viewBinding: ActivityBarcodeScanOnlyBinding by lazy { ActivityBarcodeScanOnlyBinding.inflate(layoutInflater) }
    override val rootView: View get() = viewBinding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(viewBinding.activityMainActivityLayout.toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)// On n'affiche pas l'icone "retour" dans la MainActivity
            it.setTitle(R.string.title_scan)
        }

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            replace(viewBinding.activityBarcodeScanOnlyFrameLayout.id, devAppsMainCameraXScannerFragment)
            //addToBackStack(null) // Permet de revenir aux fragments affichés précédement via le bouton back
        }

        setContentView(rootView)
    }
}