package com.devappspros.barcodescanner.domain.entity.product.foodProduct

import com.devappspros.barcodescanner.R

enum class DevAppsNovaGroup(val drawableResource: Int, val descriptionStringResource: Int) {
    GROUP_1DevApps(R.drawable.nova_group_1, R.string.nova_group_description_1),
    GROUP_2DevApps(R.drawable.nova_group_2, R.string.nova_group_description_2),
    GROUP_3DevApps(R.drawable.nova_group_3, R.string.nova_group_description_3),
    GROUP_4DevApps(R.drawable.nova_group_4, R.string.nova_group_description_4),
    UNKNOWN(-1, R.string.nova_group_description_unknown)
}