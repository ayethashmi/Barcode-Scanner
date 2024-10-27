package com.devappspros.barcodescanner.domain.library.wifiSetup.data

data class DevAppsWifiSetupData(val authType: String,
                                val name: String,
                                val password: String,
                                val isHidden: Boolean = false,
                                val anonymousIdentity: String = "",
                                val identity: String = "",
                                val eapMethod: String = "",
                                val phase2Method: String = "")