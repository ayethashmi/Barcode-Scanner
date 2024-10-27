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

package com.devappspros.barcodescanner.presentation.views.fragments.main

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.presentation.intent.createSearchUrlIntent
import com.devappspros.barcodescanner.presentation.intent.createStartActivityIntent
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsDevAppsAboutApisActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsDevAppsAboutBddActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsAboutPermissionsDescriptionActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBaseActivity
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsCustomSearchUrlListActivity
import com.devappspros.barcodescanner.presentation.views.activities.MainActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.ShortcutsActivityDevApps
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale
import kotlin.reflect.KClass

class DevAppsMainSettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity: Activity = requireActivity()
        if (activity is DevAppsBaseActivity) {
            activity.lockDeviceRotation(false)
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, null)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configureChangeLanguagePreference()
        }

        configureStartActivity(R.string.preferences_custom_search_urls_key, DevAppsCustomSearchUrlListActivity::class)
        configureStartActivity(R.string.preferences_shortcuts_key, ShortcutsActivityDevApps::class)
        configureStartActivity(R.string.preferences_remote_api_information_about_api_key, DevAppsDevAppsAboutApisActivityDevApps::class)
        configureStartActivity(R.string.preferences_about_permissions_key, DevAppsAboutPermissionsDescriptionActivityDevApps::class)
//        configureStartActivity(R.string.preferences_about_library_third_key, AboutThirdPartyLibrariesActivity::class)
        configureStartActivity(R.string.preferences_about_bdd_key, DevAppsDevAppsAboutBddActivityDevApps::class)
        configureSourceCodePreference()
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val mActivity = requireActivity()
        if (mActivity is MainActivityDevApps) {
            when (key) {
                getString(R.string.preferences_color_key) -> mActivity.updateTheme()
                getString(R.string.preferences_theme_key) -> mActivity.updateTheme()

                getString(R.string.preferences_remote_api_choose_key),
                getString(R.string.preferences_switch_history_save_duplicates_key),
                getString(R.string.preferences_switch_scan_vibrate_key),
                getString(R.string.preferences_switch_scan_bip_key),
                getString(R.string.preferences_switch_scan_screen_rotation_key),
                getString(R.string.preferences_switch_scan_barcode_copied_key),
                getString(R.string.preferences_switch_scan_add_barcode_to_the_history_key),
                getString(R.string.preferences_switch_scan_search_on_api_key),
                getString(R.string.preferences_barcode_generation_error_correction_level_key),
                getString(R.string.preferences_switch_barcode_generation_add_barcode_to_the_history_key),
                getString(R.string.preferences_search_engine_key),
                getString(R.string.preferences_display_barcode_image_results_view_key)
                -> mActivity.devAppsSettingsManager.reload()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun configureChangeLanguagePreference() {
        val pref = findPreference(getString(R.string.preferences_languages_key)) as Preference?

        if (pref != null) {
            val localList = arrayListOf(
                R.string.preferences_default to LocaleListCompat.getEmptyLocaleList(),
                R.string.locale_language_ar to LocaleListCompat.forLanguageTags("ar"),
                R.string.locale_language_bg to LocaleListCompat.forLanguageTags("bg"),
                R.string.locale_language_ca to LocaleListCompat.forLanguageTags("ca"),
                R.string.locale_language_de to LocaleListCompat.create(Locale.GERMAN),
                R.string.locale_language_en to LocaleListCompat.create(Locale.ENGLISH),
                R.string.locale_language_es to LocaleListCompat.forLanguageTags("es"),
                R.string.locale_language_eo to LocaleListCompat.forLanguageTags("eo"),
                R.string.locale_language_fr to LocaleListCompat.create(Locale.FRENCH),
                R.string.locale_language_in to LocaleListCompat.forLanguageTags("in"),
                R.string.locale_language_it to LocaleListCompat.create(Locale.ITALIAN),
                R.string.locale_language_nb_NO to LocaleListCompat.forLanguageTags("nb-NO"),
                R.string.locale_language_nl to LocaleListCompat.forLanguageTags("nl"),
                R.string.locale_language_pl to LocaleListCompat.forLanguageTags("pl"),
                R.string.locale_language_pt to LocaleListCompat.forLanguageTags("pt"),
                R.string.locale_language_pt_BR to LocaleListCompat.forLanguageTags("pt-BR"),
                R.string.locale_language_ro to LocaleListCompat.forLanguageTags("ro"),
                R.string.locale_language_ru to LocaleListCompat.forLanguageTags("ru"),
                R.string.locale_language_sr to LocaleListCompat.forLanguageTags("sr"),
                R.string.locale_language_tr to LocaleListCompat.forLanguageTags("tr"),
                R.string.locale_language_uk to LocaleListCompat.forLanguageTags("uk"),
                R.string.locale_language_zh to LocaleListCompat.create(Locale.SIMPLIFIED_CHINESE),
                R.string.locale_language_zh_Tw to LocaleListCompat.create(Locale.TRADITIONAL_CHINESE),
                R.string.locale_language_ja to LocaleListCompat.create(Locale.JAPANESE),
            )
            val currentLocales = AppCompatDelegate.getApplicationLocales()
            var selectedIndex = localList.indexOfFirst { it.second == currentLocales }
            if (selectedIndex != -1) {
                pref.setSummary(localList[selectedIndex].first)
            }
            pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val items = localList.map { requireContext().getString(it.first) }.toTypedArray()
                val savedIndex = selectedIndex
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.preferences_languages_change)
                    .setSingleChoiceItems(items, selectedIndex) { dialogInterface, index ->
                        selectedIndex = index
                        val locales = localList[index].second
                        val base = requireContext()

                        val newConfig = Configuration(base.resources.configuration).apply {
                            setLocales(locales.unwrap() as LocaleList?)
                        }

                        base.createConfigurationContext(newConfig).let { localeContext ->
                            items[0] = localeContext.getString(R.string.preferences_default)

                            (dialogInterface as Dialog).apply {
                                findViewById<ListView>(R.id.select_dialog_listview)?.adapter?.run {
                                    (this as? BaseAdapter)?.notifyDataSetChanged()
                                }
                                setTitle(localeContext.getString(R.string.preferences_languages_change))
                                findViewById<TextView>(android.R.id.button1)?.text = localeContext.getString(android.R.string.ok)
                                findViewById<TextView>(android.R.id.button2)?.text = localeContext.getString(android.R.string.cancel)
                            }
                        }
                    }
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        if (selectedIndex != savedIndex) {
                            val locales = localList[selectedIndex].second
                            AppCompatDelegate.setApplicationLocales(locales)
                        }
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
                //startActivity(createActionAppLocaleSettingsIntent())
                true
            }
        }
    }

    /*@RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun createActionAppLocaleSettingsIntent(): Intent = Intent(
        Settings.ACTION_APP_LOCALE_SETTINGS,
        Uri.fromParts("package", requireActivity().packageName, null)
    )*/

    private fun configureStartActivity(keyResource: Int, activityKClass: KClass<out Activity>) {
        val pref = findPreference(getString(keyResource)) as Preference?

        if (pref != null) {
            pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivity(activityKClass)
            }
        }
    }

    private fun configureSourceCodePreference() {
        val pref = findPreference(getString(R.string.preferences_source_code_key)) as Preference?

        if (pref != null) {
            pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val url = requireActivity().getString(R.string.source_code_link)
                val intent: Intent = createSearchUrlIntent(url)
                requireActivity().startActivity(intent)
                true
            }
        }
    }

    private fun startActivity(activityKClass: KClass<out Activity>): Boolean {
        val intent = createStartActivityIntent(requireContext(), activityKClass)
        startActivity(intent)
        return true
    }
}