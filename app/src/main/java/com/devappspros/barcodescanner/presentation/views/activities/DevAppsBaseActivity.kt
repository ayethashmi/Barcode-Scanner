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

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.devappspros.barcodescanner.domain.library.DevAppsSettingsManager
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import kotlin.reflect.KClass

abstract class DevAppsBaseActivity: AppCompatActivity() {

    val devAppsSettingsManager: DevAppsSettingsManager by inject()

    abstract val rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        val theme = devAppsSettingsManager.getTheme()
        this.setTheme(theme)
        super.onCreate(savedInstanceState)
    }

    protected fun replaceFragment(containerViewId: Int, fragment: Fragment){
        supportFragmentManager.commit {
            replace(containerViewId, fragment)
        }
    }

    protected fun replaceFragment(containerViewId: Int, fragmentClass: KClass<out Fragment>, args: Bundle? = null, tag: String? = null){
        supportFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragmentClass.java, args, tag)
            .commit()
    }

    protected fun removeFragment(fragment: Fragment){
        supportFragmentManager.commit {
            remove(fragment)
        }
    }

    fun lockDeviceRotation(value: Boolean) {
        requestedOrientation = if (value) {
            val currentOrientation: Int = resources.configuration.orientation
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            }
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            ActivityInfo.SCREEN_ORIENTATION_FULL_USER
        }
    }

    // ---- UI ----

    fun showSnackbar(
        text: String,
        anchorView: View? = null
    ) {
        Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT).apply {
            this.anchorView = anchorView
        }.show()
    }

    fun showSnackbar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        anchorView: View? = null
    ) {
        Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT).apply {
            this.anchorView = anchorView
        }.setAction(actionText, action).show()
    }

    fun showSnackbar(
        @StringRes stringRes: Int,
        anchorView: View? = null
    ) = showSnackbar(getString(stringRes), anchorView)

    // -------------------------------
    // ------ Activity Override ------
    // -------------------------------
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    // -------------------------------
}