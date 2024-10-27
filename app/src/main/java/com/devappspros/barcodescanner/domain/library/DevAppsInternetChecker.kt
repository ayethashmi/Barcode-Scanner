package com.devappspros.barcodescanner.domain.library

class DevAppsInternetChecker {

    companion object {
        private const val COMMAND = "ping -c 1 google.com"
    }

    fun isInternetAvailable(): Boolean = try{
        Runtime.getRuntime().exec(COMMAND).waitFor() == 0
    } catch (e: Exception) {
        false
    }
}