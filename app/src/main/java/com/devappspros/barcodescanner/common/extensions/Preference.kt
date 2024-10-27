package com.devappspros.barcodescanner.common.extensions

import android.os.Build
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.devappspros.barcodescanner.R

fun Preference.initializeCustomResourcesValues(holder: PreferenceViewHolder?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        holder?.itemView?.let {
            val titleView: TextView? = it.findViewById(android.R.id.title)
            titleView?.setTextAppearance(R.style.AppTheme_TextView_Appearance_Normal_Primary)

            val summaryView: TextView? = it.findViewById(android.R.id.summary)
            summaryView?.setTextAppearance(R.style.AppTheme_TextView_Appearance_Normal_Secondary)
        }
    }
}