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

package com.devappspros.barcodescanner.presentation.views.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.common.extensions.convertToString
import com.devappspros.barcodescanner.domain.library.DevAppsSettingsManager
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import kotlin.reflect.KClass

abstract class BaseFragment: Fragment() {

    val devAppsSettingsManager: DevAppsSettingsManager by inject()

    protected fun applyFragment(containerViewId: Int, fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragment)
            .commit()
    }

    protected fun applyFragment(containerViewId: Int, fragmentClass: KClass<out Fragment>, args: Bundle? = null, tag: String? = null) {
        childFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragmentClass.java, args, tag)
            .commit()
    }

    protected fun removeAllFragments() {
        childFragmentManager.fragments.forEach { fragment ->
            childFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commit()
        }
    }

    protected fun showToastText(@StringRes textResource: Int) {
        Toast.makeText(requireContext(), getString(textResource), Toast.LENGTH_SHORT).show()
    }

    protected fun showToastText(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    protected fun copyToClipboard(label: String, text: String) {
        val clipboard: ClipboardManager = get()
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }

    /**
     * @return true si le texte n'est pas null ou vide et est donc bien affiché à l'écran.
     */
    protected fun displayText(textView: TextView, layout: View, text: String?): Boolean {
        return if(text.isNullOrBlank()) {
            layout.visibility = View.GONE
            false
        } else {
            textView.text = text//.trim()
            layout.visibility = View.VISIBLE
            true
        }
    }

    protected fun displayArray(textView: TextView, layout: View, array: Array<String?>?, separator: String = ", "): Boolean {
        val text: String? = array?.convertToString(separator)
        return displayText(textView, layout, text)
    }

    protected fun displayList(textView: TextView, layout: View, list: List<String?>?, separator: String = ", "): Boolean {
        val text: String? = list?.convertToString(separator)
        return displayText(textView, layout, text)
    }

    fun hideSoftKeyboard() {
        view?.let { currentView ->
            val inputMethodManager: InputMethodManager = get()
            inputMethodManager.hideSoftInputFromWindow(
                currentView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}