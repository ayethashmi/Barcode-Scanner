package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.shortcuts.DynamicShortcutsHandler
import com.devappspros.barcodescanner.domain.entity.DevAppsDynamicShortcut
import com.devappspros.barcodescanner.domain.repositories.DevAppsDynamicShortcutRepository

class DevAppsDynamicShortcutRepositoryImpl(
    private val dynamicShortcutsHandler: DynamicShortcutsHandler
): DevAppsDynamicShortcutRepository {

    override fun getDynamicShortcuts(): List<DevAppsDynamicShortcut> {
        return dynamicShortcutsHandler.getShortcuts()
    }

    override suspend fun createShortcuts() {
        dynamicShortcutsHandler.createShortcuts()
    }

    override suspend fun updateShortcuts(shortcuts: List<DevAppsDynamicShortcut>) {
        dynamicShortcutsHandler.updateShortcuts(shortcuts)
    }
}