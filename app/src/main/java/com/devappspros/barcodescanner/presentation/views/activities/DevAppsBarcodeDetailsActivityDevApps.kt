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

package com.devappspros.barcodescanner.presentation.views.activities

import android.content.ClipboardManager
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.extensions.getDisplayName
import com.devappspros.barcodescanner.common.extensions.parcelable
import com.devappspros.barcodescanner.common.extensions.read
import com.devappspros.barcodescanner.common.utils.BARCODE_CONTENTS_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_FORMAT_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_IMAGE_BACKGROUND_COLOR_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_IMAGE_CORNER_RADIUS_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_IMAGE_DEFAULT_SIZE
import com.devappspros.barcodescanner.common.utils.BARCODE_IMAGE_FRONT_COLOR_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_IMAGE_HEIGHT_KEY
import com.devappspros.barcodescanner.common.utils.BARCODE_IMAGE_WIDTH_KEY
import com.devappspros.barcodescanner.common.utils.QR_CODE_ERROR_CORRECTION_LEVEL_KEY
import com.devappspros.barcodescanner.common.utils.showSimpleDialog
import com.devappspros.barcodescanner.databinding.ActivityBarcodeDetailsBinding
import com.devappspros.barcodescanner.domain.entity.DevAppsImageFormat
import com.devappspros.barcodescanner.domain.entity.barcode.QrCodeErrorCorrectionLevel
import com.devappspros.barcodescanner.domain.library.DevAppsBarcodeImageGeneratorProperties
import com.devappspros.barcodescanner.domain.resources.Resource
import com.devappspros.barcodescanner.presentation.intent.createActionCreateImageIntent
import com.devappspros.barcodescanner.presentation.intent.createShareImageIntent
import com.devappspros.barcodescanner.presentation.intent.createShareTextIntent
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsImageManagerViewModel
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeImageEditor.DevAppsBarcodeImageEditorFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeImageEditor.DevAppsBarcodeImageFragment
import com.google.zxing.BarcodeFormat
import ezvcard.Ezvcard
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date

class DevAppsBarcodeDetailsActivityDevApps : DevAppsBaseActivity() {

    private val devAppsImageManagerViewModel: DevAppsImageManagerViewModel by viewModel()

    private val viewBinding: ActivityBarcodeDetailsBinding by lazy { ActivityBarcodeDetailsBinding.inflate(layoutInflater) }
    override val rootView: View get() = viewBinding.root

    private val contents: String by lazy {
        getIntentStringValue() ?: error()
    }

    private var alertDialog: AlertDialog? = null

    private fun error(): String {
        Toast.makeText(this, getString(R.string.scan_error_exception_label, "Barcode contents (String) is missing"), Toast.LENGTH_LONG).show()
        return "ERROR"
    }

    private val format: BarcodeFormat by lazy {
        getBarcodeFormat()
    }

    private val qrCodeErrorCorrectionLevel: QrCodeErrorCorrectionLevel by lazy {
        getQrCodeErrorCorrectionLevel(format)
    }

    private val properties: DevAppsBarcodeImageGeneratorProperties by lazy {
        DevAppsBarcodeImageGeneratorProperties(
            contents = contents,
            format = format,
            qrCodeErrorCorrectionLevel = qrCodeErrorCorrectionLevel,
            size = BARCODE_IMAGE_DEFAULT_SIZE,
            frontColor = Color.BLACK,
            backgroundColor = Color.WHITE
        )
    }

    private val devAppsBarcodeImageFragment: DevAppsBarcodeImageFragment by lazy {
        DevAppsBarcodeImageFragment.newInstance(properties)
    }

    private val devAppsBarcodeImageEditorFragment: DevAppsBarcodeImageEditorFragment by lazy {
        DevAppsBarcodeImageEditorFragment.newInstance(properties)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(viewBinding.activityBarcodeDetailsActivityLayout.toolbar)
        supportActionBar?.title = format.getDisplayName(this)

        savedInstanceState?.let {
            properties.apply {
                frontColor = it.getInt(BARCODE_IMAGE_FRONT_COLOR_KEY, properties.frontColor)
                backgroundColor = it.getInt(BARCODE_IMAGE_BACKGROUND_COLOR_KEY, properties.backgroundColor)
                cornerRadius = it.getFloat(BARCODE_IMAGE_CORNER_RADIUS_KEY, properties.cornerRadius)
                width = it.getInt(BARCODE_IMAGE_WIDTH_KEY, properties.width)
                height = it.getInt(BARCODE_IMAGE_HEIGHT_KEY, properties.height)
            }
        }

        if(!shouldCreateFromClipboard()) {
            configureFragments()
        }

        setContentView(rootView)
    }

    private fun configureFragments() {
        replaceFragment(
            containerViewId = viewBinding.activityBarcodeDetailsImageLayout.id,
            fragment = devAppsBarcodeImageFragment,
        )

        replaceFragment(
            containerViewId = viewBinding.activityBarcodeDetailsSettingsLayout.id,
            fragment = devAppsBarcodeImageEditorFragment,
        )
    }

    private fun shouldCreateFromClipboard(): Boolean = intent?.action == "${com.devappspros.barcodescanner.BuildConfig.APPLICATION_ID}.CREATE_FROM_CLIPBOARD"

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && shouldCreateFromClipboard()) {
            changeContentsToClipboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(BARCODE_IMAGE_FRONT_COLOR_KEY, properties.frontColor)
        outState.putInt(BARCODE_IMAGE_BACKGROUND_COLOR_KEY, properties.backgroundColor)
        outState.putFloat(BARCODE_IMAGE_CORNER_RADIUS_KEY, properties.cornerRadius)
        outState.putInt(BARCODE_IMAGE_WIDTH_KEY, properties.width)
        outState.putInt(BARCODE_IMAGE_HEIGHT_KEY, properties.height)
        super.onSaveInstanceState(outState)
    }

    private fun getIntentStringValue(): String? {
        return when(intent?.action) {
            Intent.ACTION_SEND -> when (intent.type) {
                "text/plain" -> intent.getStringExtra(Intent.EXTRA_TEXT)
                "text/x-vcard" -> {
                    intent.parcelable(Intent.EXTRA_STREAM, Uri::class.java)?.read(this)?.let { vCardText: String ->
                        try {
                            Ezvcard.parse(vCardText).first()?.let { vCard ->
                                vCard.photos?.clear()
                                Ezvcard.write(vCard).prodId(false).go()
                            }
                        } catch (e: NoClassDefFoundError) {
                            showDialog(
                                titleRes = R.string.error,
                                message = getString(R.string.scan_error_exception_label, e.toString())
                            )
                            vCardText
                        } catch (e: Exception) {
                            showDialog(
                                titleRes = R.string.error,
                                message = getString(R.string.scan_error_exception_label, e.toString())
                            )
                            vCardText
                        }
                    }
                }
                "text/calendar" -> intent.parcelable(Intent.EXTRA_STREAM, Uri::class.java)?.read(this)
                else -> intent.getStringExtra(Intent.EXTRA_TEXT)
            }
            "${com.devappspros.barcodescanner.BuildConfig.APPLICATION_ID}.CREATE_FROM_CLIPBOARD" -> getClipboardContent()
            else -> intent.getStringExtra(BARCODE_CONTENTS_KEY)
        }
    }

    private fun getBarcodeFormat(): BarcodeFormat {
        val barcodeFormatString: String = intent.getStringExtra(BARCODE_FORMAT_KEY) ?: BarcodeFormat.QR_CODE.name
        return BarcodeFormat.valueOf(barcodeFormatString)
    }

    private fun getQrCodeErrorCorrectionLevel(barcodeFormat: BarcodeFormat): QrCodeErrorCorrectionLevel {
        return when(barcodeFormat) {
            BarcodeFormat.QR_CODE -> {
                val qrCodeErrorCorrectionLevelString: String? = intent.getStringExtra(QR_CODE_ERROR_CORRECTION_LEVEL_KEY)
                if(qrCodeErrorCorrectionLevelString != null) {
                    QrCodeErrorCorrectionLevel.valueOf(qrCodeErrorCorrectionLevelString)
                } else {
                    devAppsSettingsManager.getQrCodeErrorCorrectionLevel()
                }
            }
            else -> QrCodeErrorCorrectionLevel.NONE
        }
    }

    // ---- Barcode Bitmap Creator ----

    // Call by Fragments
    fun regenerateBitmap(
        width: Int = properties.width,
        height: Int = properties.height,
        @ColorInt frontColor: Int = properties.frontColor,
        @ColorInt backgroundColor: Int = properties.backgroundColor,
        cornerRadius: Float = properties.cornerRadius
    ) {
        devAppsBarcodeImageFragment.generateNewBarcodeBitmap(
            properties.apply {
                this.width = width
                this.height = height
                this.frontColor = frontColor
                this.backgroundColor = backgroundColor
                this.cornerRadius = cornerRadius
            }
        )
    }

    // ---- Menu contenant les items permettant de sauvegarder ou partager le QrCode ----

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_barcode_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_activity_barcode_details_save_image_png -> startExportation(DevAppsImageFormat.PNG)
            R.id.menu_activity_barcode_details_save_image_jpg -> startExportation(DevAppsImageFormat.JPG)
            R.id.menu_activity_barcode_details_save_image_svg -> startExportation(DevAppsImageFormat.SVG)
            R.id.menu_activity_barcode_details_share_image -> shareImage()
            R.id.menu_activity_barcode_details_share_text -> shareText()
        }
        return super.onOptionsItemSelected(item)
    }

    // ---- Export image ----

    private val result: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val uri = it.data?.data
            if(uri != null) {
                exportAndObserve(uri)
            }
        }

    private var devAppsImageFormat: DevAppsImageFormat = DevAppsImageFormat.PNG

    private fun startExportation(devAppsImageFormat: DevAppsImageFormat) {
        this.devAppsImageFormat = devAppsImageFormat
        val date = get<Date>()
        val simpleDateFormat = get<SimpleDateFormat> { parametersOf("yyyy-MM-dd-HH-mm-ss") }
        val dateNameStr = simpleDateFormat.format(date)
        val name = "barcode_$dateNameStr"

        val intent: Intent = createActionCreateImageIntent(name, devAppsImageFormat.mimeType)
        result.launch(intent)
    }

    private fun exportAndObserve(uri: Uri) {
        export(uri).observe(this) { response ->
            when(response) {
                is Resource.Progress -> {}
                is Resource.Success -> {
                    when(response.data) {
                        true -> showSnackbar(R.string.snack_bar_message_save_bitmap_ok)
                        else-> showSnackbar(R.string.snack_bar_message_save_bitmap_error)
                    }
                }
                is Resource.Failure -> showSnackbar(R.string.snack_bar_message_save_bitmap_error)
            }
        }
    }

    private fun export(uri: Uri): LiveData<Resource<Boolean>> {
        return when(devAppsImageFormat) {
            DevAppsImageFormat.PNG -> devAppsImageManagerViewModel.exportAsPng(devAppsBarcodeImageFragment.bitmap, uri)
            DevAppsImageFormat.JPG -> devAppsImageManagerViewModel.exportAsJpg(devAppsBarcodeImageFragment.bitmap, uri)
            DevAppsImageFormat.SVG -> devAppsImageManagerViewModel.exportAsSvg(properties, uri)
        }
    }

    // ---- Share image / text ----

    private fun shareImage() {
        devAppsImageManagerViewModel.shareBitmap(devAppsBarcodeImageFragment.bitmap).observe(this) {
            when(it) {
                is Resource.Progress -> {}
                is Resource.Success -> {
                    when(it.data) {
                        null -> showSnackbar(R.string.snack_bar_message_share_bitmap_error)
                        else -> {
                            val intent: Intent = createShareImageIntent(applicationContext, it.data)
                            startActivity(intent)
                        }
                    }
                }
                is Resource.Failure -> showSnackbar(R.string.snack_bar_message_share_bitmap_error)
            }
        }
    }

    private fun shareText() {
        val intent: Intent = createShareTextIntent(applicationContext, contents)
        startActivity(intent)
    }

    // ---- AlertDialog ----

    private fun showDialog(@StringRes titleRes: Int, message: String, listener: OnClickListener? = null) {
        alertDialog = showSimpleDialog(this, titleRes, message, listener)
    }

    // ---- Clipboard ----

    private fun changeContentsToClipboard() {
        val text = getClipboardContent()

        if (text.isNullOrEmpty()) {
            showDialog(
                titleRes = R.string.error,
                message = getString(R.string.clipboard_empty),
                listener = { _, _ -> finishAndRemoveTask() }
            )
        } else {
            // Avoid errors when rotating the screen triggered by savedInstanceState: Bundle? in onCreate(savedInstanceState: Bundle?).
            // Also, avoid having to retrieve the clipboard content again by passing through onWindowFocusChanged(hasFocus: Boolean).
            intent.apply {
                action = null
                putExtra(BARCODE_CONTENTS_KEY, text)
            }

            configureFragments()
        }
    }

    private fun getClipboardContent(): String? {
        val clipboard: ClipboardManager = get()
        if (clipboard.hasPrimaryClip()) {
            val data = clipboard.primaryClip
            if ((data?.itemCount ?: 0) > 0) {
                val text = data?.getItemAt(0)?.coerceToText(this)?.trim() ?: ""
                if (text.isNotEmpty()) {
                    return text.toString()
                }
            }
        }

        // The clipboard is empty, or we do not have access to see it
        return null
    }
}