package com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.foodProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.utils.BARCODE_ANALYSIS_KEY
import com.devappspros.barcodescanner.databinding.FragmentFoodAnalysisBinding
import com.devappspros.barcodescanner.domain.entity.analysis.DevAppsFoodDevAppsBarcodeAnalysis
import com.devappspros.barcodescanner.presentation.views.adapters.DevAppsFragmentPagerAdapter
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.DevAppsApiAnalysisFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.foodProduct.ingredients.DevAppsFoodAnalysisRootIngredientsFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.foodProduct.nutritionFacts.DevAppsFoodAnalysisRootNutritionFactsFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.analysis.foodProduct.overview.DevAppsFoodAnalysisRootOverviewFragmentDevApps
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.get

/**
 * A simple [Fragment] subclass.
 */
class DevAppsFoodAnalysisFragmentDevAppsDevApps: DevAppsApiAnalysisFragmentDevApps<DevAppsFoodDevAppsBarcodeAnalysis>() {

    private var _binding: FragmentFoodAnalysisBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodAnalysisBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun start(analysis: DevAppsFoodDevAppsBarcodeAnalysis) {
        super.start(analysis)

        // Permet de pré-charger les deux Fragments supplémentaires du ViewPager
        viewBinding.fragmentFoodAnalysisViewPager.offscreenPageLimit = 2

        configureFoodProductView(analysis)
    }

    private fun configureFoodProductView(barcodeAnalysis: DevAppsFoodDevAppsBarcodeAnalysis){

        val overviewFragment = DevAppsFoodAnalysisRootOverviewFragmentDevApps.newInstance(barcodeAnalysis)
        val ingredientsFragment = DevAppsFoodAnalysisRootIngredientsFragmentDevApps.newInstance(barcodeAnalysis)
        val nutritionFragment = DevAppsFoodAnalysisRootNutritionFactsFragmentDevApps.newInstance(barcodeAnalysis)
        val adapter = DevAppsFragmentPagerAdapter(childFragmentManager, lifecycle, overviewFragment, ingredientsFragment, nutritionFragment)

        val overview: String = getString(R.string.overview_tab_label)
        val ingredients: String = getString(R.string.ingredients_label)
        val nutrition: String = getString(R.string.nutrition_facts_tab_label)
        configureViewPager(adapter, overview, ingredients, nutrition)
    }

    // ---- ViewPager Configuration ----
    private fun configureViewPager(adapter: FragmentStateAdapter, vararg textTab: String){

        val viewPager = viewBinding.fragmentFoodAnalysisViewPager
        val tabLayout = viewBinding.fragmentFoodAnalysisTabLayout

        viewPager.adapter=adapter

        if(textTab.isNotEmpty()) {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = textTab[position]
            }.attach()

            tabLayout.visibility = View.VISIBLE
        }else{
            tabLayout.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(foodBarcodeAnalysis: DevAppsFoodDevAppsBarcodeAnalysis) = DevAppsFoodAnalysisFragmentDevAppsDevApps().apply {
            arguments = get<Bundle>().apply {
                putSerializable(BARCODE_ANALYSIS_KEY, foodBarcodeAnalysis)
            }
        }
    }
}