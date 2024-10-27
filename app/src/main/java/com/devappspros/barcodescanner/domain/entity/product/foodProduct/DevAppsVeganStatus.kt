package com.devappspros.barcodescanner.domain.entity.product.foodProduct

import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.devappspros.barcodescanner.R

enum class DevAppsVeganStatus(@StringRes val stringResource: Int, @AttrRes val colorResource: Int) {
    DevAppsVEGAN(R.string.is_vegan_label, R.attr.colorPositive),
    NO_DevApps_VEGAN(R.string.no_vegan_label, R.attr.colorNegative),
    MAYBE_DevApps_VEGAN(R.string.maybe_vegan_label, R.attr.colorMedium),
    UNKNOWN_DevApps_VEGAN(R.string.vegan_status_unknown_label, R.attr.colorUnknown)
}