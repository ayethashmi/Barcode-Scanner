/*
 * Barcode Scanner
 * Copyright (C) 2021  Atharok
 *
 * This file is part of Barcode Scanner.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.foodProduct.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.fixAnimateLayoutChangesInNestedScroll
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.databinding.FragmentFoodAnalysisRootOverviewBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsEcoScore
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNovaGroup
import com.devappspros.barcodescanner.domain.entity.product.foodProduct.DevAppsNutriscore
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.DevAppsBarcodeAnalysisFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.about.DevAppsDevAppsBarcodeAboutOverviewFragment
import com.devappspros.barcodescanner.presentation.views.fragments.templates.ProductOverviewFragment
import org.koin.android.ext.android.get

/**
 * A simple [Fragment] subclass.
 */
class DevAppsFoodAnalysisRootOverviewFragmentDevApps : DevAppsBarcodeAnalysisFragment<DevAppsFoodDevAppsBarcodeAnalysis>() {

    private var _binding: FragmentFoodAnalysisRootOverviewBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodAnalysisRootOverviewBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsFoodDevAppsBarcodeAnalysis) {
        viewBinding.fragmentFoodAnalysisRootOverviewOuterView.fixAnimateLayoutChangesInNestedScroll()

        configureProductOverviewFragment(analysis)
        applyFragment(viewBinding.fragmentFoodAnalysisRootOverviewVeggieAnalysisFrameLayout.id, DevAppsFoodAnalysisVeggieFragmentDevApps::class, arguments)
        configureQuality(analysis)
        configureDetails(analysis)
        configureBarcodeAboutOverviewFragment()
    }

    // ---- Overview ----

    private fun configureProductOverviewFragment(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) {
        val fragment = ProductOverviewFragment.newInstance(
            imageUrl = foodProduct.imageFrontUrl,
            title = foodProduct.name ?: getString(R.string.bar_code_type_unknown_product),
            subtitle1 = foodProduct.brands,
            subtitle2 = foodProduct.quantity
        )
        applyFragment(viewBinding.fragmentFoodAnalysisRootOverviewOverviewFrameLayout.id, fragment)
    }

    // ---- Quality ----

    private fun configureQuality(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis){
        val devAppsNutriscore: DevAppsNutriscore = foodProduct.devAppsNutriscore
        val devAppsNovaGroup: DevAppsNovaGroup = foodProduct.devAppsNovaGroup
        val devAppsEcoScore: DevAppsEcoScore = foodProduct.devAppsEcoScore

        if(devAppsNutriscore == DevAppsNutriscore.UNKNOWN && devAppsNovaGroup == DevAppsNovaGroup.UNKNOWN && devAppsEcoScore == DevAppsEcoScore.UNKNOWN) {
            viewBinding.fragmentFoodAnalysisRootOverviewQualityEntitledTextView.visibility = View.GONE
        }else {
            configureQualityEntitled()

            if (devAppsNutriscore != DevAppsNutriscore.UNKNOWN) {
                configureNutriScoreFragment(devAppsNutriscore)
            } else {
                viewBinding.fragmentFoodAnalysisRootOverviewQualityNutriScoreFrameLayout.visibility = View.GONE
            }

            if (devAppsNovaGroup != DevAppsNovaGroup.UNKNOWN) {
                configureNovaGroupFragment(devAppsNovaGroup)
            } else {
                viewBinding.fragmentFoodAnalysisRootOverviewQualityNovaGroupFrameLayout.visibility = View.GONE
            }

            if (devAppsEcoScore != DevAppsEcoScore.UNKNOWN) {
                configureEcoScoreFragment(devAppsEcoScore)
            } else {
                viewBinding.fragmentFoodAnalysisRootOverviewQualityEcoScoreFrameLayout.visibility = View.GONE
            }
        }
    }

    private fun configureQualityEntitled() {
        viewBinding.fragmentFoodAnalysisRootOverviewQualityEntitledTextView.visibility = View.VISIBLE
    }

    private fun configureNutriScoreFragment(devAppsNutriscore: DevAppsNutriscore) = configureQualityFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisRootOverviewQualityNutriScoreFrameLayout,
        title = getString(R.string.nutriscore_entitled_label),
        subtitle = getString(devAppsNutriscore.descriptionStringResource),
        description = getString(R.string.nutriscore_description),
        drawableRes = devAppsNutriscore.drawableResource
    )

    private fun configureNovaGroupFragment(noveGroup: DevAppsNovaGroup) = configureQualityFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisRootOverviewQualityNovaGroupFrameLayout,
        title = getString(R.string.nova_group_entitled_label),
        subtitle = getString(noveGroup.descriptionStringResource),
        description = getString(R.string.nova_group_description),
        drawableRes = noveGroup.drawableResource
    )

    private fun configureEcoScoreFragment(devAppsEcoScore: DevAppsEcoScore) = configureQualityFragment(
        frameLayout = viewBinding.fragmentFoodAnalysisRootOverviewQualityEcoScoreFrameLayout,
        title = getString(R.string.eco_score_entitled_label),
        subtitle = getString(devAppsEcoScore.descriptionStringResource),
        description = getString(R.string.eco_score_description),
        drawableRes = devAppsEcoScore.drawableResource
    )

    private fun configureQualityFragment(frameLayout: FrameLayout, title: String, subtitle: String, description: String, drawableRes: Int) {
        val fragment = DevAppsFoodAnalysisQualityFragment.newInstance(
            drawableRes = drawableRes,
            title = title,
            subtitle = subtitle,
            description = description
        )
        applyFragment(frameLayout.id, fragment)
    }

    // ---- Details ----

    private fun configureDetails(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) {
        val categories: String? = foodProduct.categories
        val packaging: String? = foodProduct.packaging
        val stores: String? = foodProduct.stores
        val countries: List<String>? = foodProduct.countriesTagList
        val labels: String? = foodProduct.labels
        val labelsTags: List<String>? = foodProduct.labelsTagList

        if(categories.isNullOrBlank() && packaging.isNullOrBlank() && stores.isNullOrBlank() &&
            countries.isNullOrEmpty() && labels.isNullOrBlank() && labelsTags.isNullOrEmpty()) {
            viewBinding.fragmentFoodAnalysisRootOverviewDetailsEntitledTextView.visibility = View.GONE
        } else {
            viewBinding.fragmentFoodAnalysisRootOverviewDetailsEntitledTextView.visibility = View.VISIBLE

            // Labels Fragment
            if (!labels.isNullOrBlank() || !labelsTags.isNullOrEmpty())
                applyFragment(viewBinding.fragmentFoodAnalysisRootOverviewLabelsFrameLayout.id, DevAppsFoodAnalysisLabelsFragmentDevApps::class, arguments)

            // Details Fragment
            applyFragment(viewBinding.fragmentFoodAnalysisRootOverviewDetailsFrameLayout.id, DevAppsFoodAnalysisDetailsFragmentDevApps::class, arguments)
        }
    }

    // ---- Barcode ----

    private fun configureBarcodeAboutOverviewFragment() = applyFragment(
        containerViewId = viewBinding.fragmentFoodAnalysisRootOverviewBarcodeAboutOverviewFrameLayout.id,
        fragmentClass = DevAppsDevAppsBarcodeAboutOverviewFragment::class,
        args = arguments
    )

    companion object {
        fun newInstance(foodProduct: DevAppsFoodDevAppsBarcodeAnalysis) = DevAppsFoodAnalysisRootOverviewFragmentDevApps().apply {
            arguments = get<Bundle>().apply {
                putSerializable(BARCODE_ANALYSIS_KEY, foodProduct)
            }
        }
    }
}
