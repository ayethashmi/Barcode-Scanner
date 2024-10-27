package com.devappspros.barcodescanner.data.shortcuts

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.domain.entity.DevAppsDynamicShortcut
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeDetailsActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeScanFromImageShortcutActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.MainActivityDevApps

class DynamicShortcutsHandler(
    private val context: Context
) {

    companion object {
        private const val SCAN_ID = "scan"
        private const val SCAN_FROM_IMAGE_ID = "scanFromImage"
        private const val HISTORY_ID = "history"
        private const val CREATE_ID = "create"
        private const val CREATE_FROM_CLIPBOARD_ID = "createFromClipboard"
    }

    private val map: Map<String, DevAppsDynamicShortcut> by lazy {
        mapOf(
            SCAN_ID to DevAppsDynamicShortcut(
                id = SCAN_ID,
                label = R.string.title_scan,
                drawable = R.drawable.ic_shortcut_scan,
                icon = R.drawable.baseline_qr_code_scanner_24,
                targetClass = MainActivityDevApps::class.java,
                action = "${com.devappspros.barcodescanner.BuildConfig.APPLICATION_ID}.SCAN"
            ),
            SCAN_FROM_IMAGE_ID to DevAppsDynamicShortcut(
                id = SCAN_FROM_IMAGE_ID,
                label = R.string.intent_filter_scan_by_image,
                drawable = R.drawable.ic_shortcut_scan_from_image,
                icon = R.drawable.baseline_image_24,
                targetClass = DevAppsBarcodeScanFromImageShortcutActivityDevApps::class.java,
                action = Intent.ACTION_VIEW
            ),
            HISTORY_ID to DevAppsDynamicShortcut(
                id = HISTORY_ID,
                label = R.string.title_history,
                drawable = R.drawable.ic_shortcut_history,
                icon = R.drawable.baseline_history_24,
                targetClass = MainActivityDevApps::class.java,
                action = "${com.devappspros.barcodescanner.BuildConfig.APPLICATION_ID}.HISTORY"
            ),
            CREATE_ID to DevAppsDynamicShortcut(
                id = CREATE_ID,
                label = R.string.title_bar_code_creator,
                drawable = R.drawable.ic_shortcut_create,
                icon = R.drawable.baseline_create_24,
                targetClass = MainActivityDevApps::class.java,
                action = "${com.devappspros.barcodescanner.BuildConfig.APPLICATION_ID}.CREATE"
            ),
            CREATE_FROM_CLIPBOARD_ID to DevAppsDynamicShortcut(
                id = CREATE_FROM_CLIPBOARD_ID,
                label = R.string.create_qr_from_clipboard,
                drawable = R.drawable.ic_shortcut_create_from_clipboard,
                icon = R.drawable.outline_content_paste_go_24,
                targetClass = DevAppsBarcodeDetailsActivityDevApps::class.java,
                action = "${com.devappspros.barcodescanner.BuildConfig.APPLICATION_ID}.CREATE_FROM_CLIPBOARD"
            )
        )
    }

    fun createShortcuts() {
        val shortcuts = ShortcutManagerCompat.getDynamicShortcuts(context)
        if (shortcuts.isEmpty() || shortcuts.size != map.size) {
            ShortcutManagerCompat.removeAllDynamicShortcuts(context)
            createShortcut(0, map[SCAN_ID])
            createShortcut(1, map[SCAN_FROM_IMAGE_ID])
            createShortcut(2, map[HISTORY_ID])
            createShortcut(3, map[CREATE_ID])
            createShortcut(4, map[CREATE_FROM_CLIPBOARD_ID])
        }
    }

    fun updateShortcuts(shortcuts: List<DevAppsDynamicShortcut>) {
        ShortcutManagerCompat.removeAllDynamicShortcuts(context)
        shortcuts.forEachIndexed { index, shortcut ->
            createShortcut(index, shortcut)
        }
    }

    fun getShortcuts(): List<DevAppsDynamicShortcut> = ShortcutManagerCompat
        .getDynamicShortcuts(context)
        .sortedBy { it.rank }
        .mapNotNull { map[it.id] }

    private fun createShortcut(rank: Int, shortcut: DevAppsDynamicShortcut?) {
        shortcut?.let {
            val shortcutInfoCompat = ShortcutInfoCompat.Builder(context, shortcut.id)
                .setShortLabel(context.getString(shortcut.label))
                .setLongLabel(context.getString(shortcut.label))
                .setIcon(IconCompat.createWithResource(context, shortcut.drawable))
                .setIntent(
                    Intent(context, shortcut.targetClass).setAction(shortcut.action)
                )
                .setRank(rank)
                .build()

            ShortcutManagerCompat.pushDynamicShortcut(context, shortcutInfoCompat)
        }
    }
}