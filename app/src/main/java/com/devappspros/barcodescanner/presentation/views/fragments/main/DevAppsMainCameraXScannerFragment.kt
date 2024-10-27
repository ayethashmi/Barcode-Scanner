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

package com.devappspros.barcodescanner.presentation.views.fragments.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.SCAN_RESULT
import com.devappspros.barcodescanner.common.extensions.SCAN_RESULT_ERROR_CORRECTION_LEVEL
import com.devappspros.barcodescanner.common.extensions.SCAN_RESULT_FORMAT
import com.devappspros.barcodescanner.common.extensions.toIntent
import com.devappspros.barcodescanner.common.utils.BARCODE_KEY
import com.devappspros.barcodescanner.common.utils.KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_RESULT
import com.devappspros.barcodescanner.common.utils.KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_STRING
import com.devappspros.barcodescanner.databinding.FragmentMainCameraXScannerBinding
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.QrCodeErrorCorrectionLevel
import com.devappspros.barcodescanner.domain.library.DevAppsBeepManager
import com.devappspros.barcodescanner.domain.library.DevAppsVibratorAppCompat
import com.devappspros.barcodescanner.domain.library.camera.DevAppsAbstractCameraXBarcodeAnalyzer
import com.devappspros.barcodescanner.domain.library.camera.DevAppsCameraConfig
import com.devappspros.barcodescanner.domain.library.camera.DevAppsCameraXBarcodeAnalyzerDevApps
import com.devappspros.barcodescanner.domain.library.camera.DevAppsCameraXBarcodeLegacyAnalyzerDevApps
import com.devappspros.barcodescanner.domain.library.camera.DevAppsCameraZoomGestureDetector
import com.devappspros.barcodescanner.presentation.intent.createStartActivityIntent
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDatabaseBarcodeViewModel
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeAnalysisActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeScanFromImageGalleryActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBaseActivity
import com.devappspros.barcodescanner.presentation.views.activities.MainActivityDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.BaseFragment
import com.google.zxing.Result
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

/**
 * A simple [Fragment] subclass.
 */
class DevAppsMainCameraXScannerFragment : BaseFragment(), DevAppsAbstractCameraXBarcodeAnalyzer.BarcodeDetector {

    companion object {
        private const val ZXING_SCAN_INTENT_ACTION = "com.google.zxing.client.android.SCAN"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private var devAppsCameraConfig: DevAppsCameraConfig? = null
    private val devAppsDatabaseBarcodeViewModel: DevAppsDatabaseBarcodeViewModel by activityViewModel()

    // ---- View ----
    private var _binding: FragmentMainCameraXScannerBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureResultBarcodeScanFromImageActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainCameraXScannerBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureMenu()
        askPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        devAppsCameraConfig?.stopCamera()
        _binding=null
    }

    override fun onResume() {
        super.onResume()

        if (allPermissionsGranted()) {
            doPermissionGranted()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity: Activity = requireActivity()
        if(activity is DevAppsBaseActivity) {
            if(devAppsSettingsManager.isAutoScreenRotationScanDisabled) {
                activity.lockDeviceRotation(true)
            }
        }
    }

    // ---- Menu ----

    private fun configureMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_scanner, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
                R.id.menu_scanner_flash -> {
                    devAppsCameraConfig?.switchFlash()
                    requireActivity().invalidateOptionsMenu()
                    true
                }
                R.id.menu_scanner_scan_from_image -> {
                    startBarcodeScanFromImageActivity()
                    true
                }
                else -> false
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)

                if(devAppsCameraConfig?.hasFlash()==true && allPermissionsGranted()) {
                    if (devAppsCameraConfig?.flashEnabled == true) {
                        menu.getItem(0).icon =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.baseline_flash_on_24
                            )
                    } else {
                        menu.getItem(0).icon =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.baseline_flash_off_24
                            )
                    }

                } else {
                    menu.getItem(0).isVisible = false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    // ---- Camera Permission ----

    private fun askPermission() {
        if (!allPermissionsGranted()) {
            // Gère le resultat de la demande de permission d'accès à la caméra.
            val requestPermission: ActivityResultLauncher<String> =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    if (it) {
                        doPermissionGranted()
                    } else doPermissionRefused()
                }
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun doPermissionGranted() {
        configureCamera()
        viewBinding.fragmentMainCameraXScannerCameraPermissionTextView.visibility = View.GONE
        viewBinding.fragmentMainCameraXScannerPreviewView.visibility = View.VISIBLE
        viewBinding.fragmentMainCameraXScannerScanOverlay.visibility = View.VISIBLE
        viewBinding.fragmentMainCameraXScannerSlider.visibility = View.VISIBLE
        viewBinding.fragmentMainCameraXScannerInformationTextView?.visibility = View.VISIBLE
    }

    private fun doPermissionRefused() {
        devAppsCameraConfig?.stopCamera()
        viewBinding.fragmentMainCameraXScannerCameraPermissionTextView.visibility = View.VISIBLE
        viewBinding.fragmentMainCameraXScannerPreviewView.visibility = View.GONE
        viewBinding.fragmentMainCameraXScannerScanOverlay.visibility = View.GONE
        viewBinding.fragmentMainCameraXScannerSlider.visibility = View.GONE
        viewBinding.fragmentMainCameraXScannerInformationTextView?.visibility = View.GONE
    }

    // ---- Camera ----

    private fun configureCamera() {
        devAppsCameraConfig = DevAppsCameraConfig(requireContext()).apply {

            val analyzer: DevAppsAbstractCameraXBarcodeAnalyzer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DevAppsCameraXBarcodeAnalyzerDevApps(this@DevAppsMainCameraXScannerFragment)
            } else {
                DevAppsCameraXBarcodeLegacyAnalyzerDevApps(this@DevAppsMainCameraXScannerFragment)
            }

            this.setAnalyzer(analyzer)
            this.startCamera(
                lifecycleOwner = this@DevAppsMainCameraXScannerFragment as LifecycleOwner,
                previewView = viewBinding.fragmentMainCameraXScannerPreviewView
            )
            this@DevAppsMainCameraXScannerFragment.configureZoom(this)
        }
    }

    override fun onBarcodeFound(result: Result) {
        viewBinding.fragmentMainCameraXScannerPreviewView.post {
            if(devAppsCameraConfig?.isRunning() == true) {
                devAppsCameraConfig?.stopCamera()
                onSuccessfulScanFromCamera(result)
            }
        }
    }

    override fun onError(msg: String) {
        viewBinding.fragmentMainCameraXScannerPreviewView.post {
            devAppsCameraConfig?.stopCamera()
            viewBinding.fragmentMainCameraXScannerCameraPermissionTextView.text = getString(R.string.scan_error_exception_label, msg)
            doPermissionRefused()
        }
    }

    private fun configureZoom(devAppsCameraConfig: DevAppsCameraConfig) {
        val slider = viewBinding.fragmentMainCameraXScannerSlider
        slider.value = devAppsSettingsManager.getDefaultZoomValue()/100f
        devAppsCameraConfig.setLinearZoom(slider.value)
        slider.addOnChangeListener { v, value, _ ->
            devAppsCameraConfig.setLinearZoom(value)
            // BZZZTT!!1!
            v.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
        }
        DevAppsCameraZoomGestureDetector(slider.value)
            .attach(viewBinding.fragmentMainCameraXScannerScanOverlay) { value ->
                slider.value = value
            }
    }

    // ---- Scan successful ----

    private inline fun onSuccessfulScan(
        contents: String?,
        formatName: String?,
        errorCorrectionLevel: QrCodeErrorCorrectionLevel,
        crossinline onResult: (barcode: Barcode) -> Unit
    ) = requireActivity().runOnUiThread {

        if(contents != null && formatName != null) {

            if(devAppsSettingsManager.shouldCopyBarcodeScan) {
                copyToClipboard("contents", contents)
                showToastText(R.string.barcode_copied_label)
            }

            if(devAppsSettingsManager.useBipScan)
                get<DevAppsBeepManager>().playBeepSound(requireActivity())

            if(devAppsSettingsManager.useVibrateScan)
                get<DevAppsVibratorAppCompat>().vibrate()

            val barcode: Barcode = get { parametersOf(contents, formatName, errorCorrectionLevel) }

            if(devAppsSettingsManager.shouldAddBarcodeScanToHistory) {
                // Insert les informations du code-barres dans la base de données (de manière asynchrone)
                devAppsDatabaseBarcodeViewModel.insertBarcode(barcode, devAppsSettingsManager.saveDuplicates)
            }

            onResult(barcode)
        } else {
            showSnackbar(getString(R.string.scan_cancel_label))
        }
    }

    // ---- Scan from Camera ----

    private fun onSuccessfulScanFromCamera(result: Result) {
        val contents = result.text
        val formatName = result.barcodeFormat?.name
        val errorCorrectionLevel: QrCodeErrorCorrectionLevel =
            get(named(KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_RESULT)) { parametersOf(result) }

        onSuccessfulScan(contents, formatName, errorCorrectionLevel) { barcode ->
            // Si l'application a été ouverte via une application tierce
            if (requireActivity().intent?.action == ZXING_SCAN_INTENT_ACTION) {
                sendResultToAppIntent(result.toIntent())
            } else {
                startBarcodeAnalysisActivity(barcode)
            }
        }
    }

    /**
     * Démarre l'Activity: BarcodeAnalysisActivity.
     */
    private fun startBarcodeAnalysisActivity(barcode: Barcode) {
        val intent = createStartActivityIntent(requireContext(), DevAppsBarcodeAnalysisActivityDevApps::class).apply {
            putExtra(BARCODE_KEY, barcode)
        }
        startActivity(intent)
    }

    // ---- Scan from Image ----

    private var resultBarcodeScanFromImageActivity: ActivityResultLauncher<Intent>? = null

    private fun configureResultBarcodeScanFromImageActivity(){
        resultBarcodeScanFromImageActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                it.data?.let { intentResult ->
                    onSuccessfulScanFromImage(intentResult)
                }
            }
        }
    }

    private fun onSuccessfulScanFromImage(intentResult: Intent) {
        val contents = intentResult.getStringExtra(SCAN_RESULT)
        val formatName = intentResult.getStringExtra(SCAN_RESULT_FORMAT)
        val errorCorrectionLevel: QrCodeErrorCorrectionLevel =
            get(named(KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_STRING)) {
                parametersOf(intentResult.getStringExtra(SCAN_RESULT_ERROR_CORRECTION_LEVEL))
            }

        onSuccessfulScan(contents, formatName, errorCorrectionLevel) { barcode ->
            // Si l'application a été ouverte via une application tierce
            if (requireActivity().intent?.action == ZXING_SCAN_INTENT_ACTION) {
                sendResultToAppIntent(intentResult)
            } else {
                startBarcodeAnalysisActivity(barcode)
            }
        }
    }

    private fun startBarcodeScanFromImageActivity() {
        devAppsCameraConfig?.stopCamera()
        resultBarcodeScanFromImageActivity?.let { result ->
            val intent = createStartActivityIntent(requireContext(), DevAppsBarcodeScanFromImageGalleryActivityDevApps::class)
            result.launch(intent)
        }
    }

    // ---- Snackbar ----

    private fun showSnackbar(text: String) {
        val activity = requireActivity()
        if(activity is MainActivityDevApps) {
            activity.showSnackbar(text)
        }
    }

    // ---- Intent ----

    private fun sendResultToAppIntent(intent: Intent) {
        requireActivity().apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}