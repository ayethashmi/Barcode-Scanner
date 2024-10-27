package com.devappspros.barcodescanner.domain.entity.product.foodProduct

import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.devappspros.barcodescanner.R

enum class DevAppsPalmOilStatus(@StringRes val stringResource: Int, @AttrRes val colorResource: Int) {
    PALM_OIL_FREE(R.string.palm_oil_free_label, R.attr.colorPositive),
    PALM_OIL(R.string.contain_palm_oil_label, R.attr.colorNegative),
    MAYBE_PALM_OIL(R.string.may_contain_palm_oil_label, R.attr.colorMedium),
    UNKNOWN_PALM_OIL(R.string.palm_oil_content_unknown_label, R.attr.colorUnknown)
}