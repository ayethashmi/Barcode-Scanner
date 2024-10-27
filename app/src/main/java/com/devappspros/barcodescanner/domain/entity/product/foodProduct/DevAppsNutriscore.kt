package com.devappspros.barcodescanner.domain.entity.product.foodProduct

import com.devappspros.barcodescanner.R

enum class DevAppsNutriscore(val drawableResource: Int, val descriptionStringResource: Int) {
    A(R.drawable.nutriscore_a, R.string.nutriscore_description_a),
    B(R.drawable.nutriscore_b, R.string.nutriscore_description_b),
    C(R.drawable.nutriscore_c, R.string.nutriscore_description_c),
    D(R.drawable.nutriscore_d, R.string.nutriscore_description_d),
    E(R.drawable.nutriscore_e, R.string.nutriscore_description_e),
    UNKNOWN(-1, R.string.nutriscore_description_unknown)
}