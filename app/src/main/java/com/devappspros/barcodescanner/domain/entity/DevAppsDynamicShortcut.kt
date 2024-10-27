package com.devappspros.barcodescanner.domain.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class DevAppsDynamicShortcut(
    val id: String,
    @StringRes val label: Int,
    @DrawableRes val drawable: Int,
    @DrawableRes val icon: Int,
    val targetClass: Class<*>,
    val action: String
)