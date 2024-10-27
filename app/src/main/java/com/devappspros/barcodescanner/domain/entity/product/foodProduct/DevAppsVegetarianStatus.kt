package com.devappspros.barcodescanner.domain.entity.product.foodProduct

import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.devappspros.barcodescanner.R

enum class DevAppsVegetarianStatus(@StringRes val stringResource: Int, @AttrRes val colorResource: Int) {
    DevAppsVEGETARIAN(R.string.is_vegetarian_label, R.attr.colorPositive),
    NO_DevApps_VEGETARIAN(R.string.no_vegetarian_label, R.attr.colorNegative),
    MAYBE_DevApps_VEGETARIAN(R.string.maybe_vegetarian_label, R.attr.colorMedium),
    UNKNOWN_DevApps_VEGETARIAN(R.string.vegetarian_status_unknown_label, R.attr.colorUnknown)
}