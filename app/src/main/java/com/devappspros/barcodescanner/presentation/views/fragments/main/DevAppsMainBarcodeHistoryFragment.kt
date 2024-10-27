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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.utils.BARCODE_KEY
import com.devappspros.barcodescanner.databinding.FragmentMainHistoryBinding
import com.devappspros.barcodescanner.domain.entity.DevAppsFileFormat
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.resources.Resource
import com.devappspros.barcodescanner.presentation.customView.DevAppsCustomItemTouchHelperCallback
import com.devappspros.barcodescanner.presentation.customView.DevAppsMarginItemDecoration
import com.devappspros.barcodescanner.presentation.intent.createActionCreateFileIntent
import com.devappspros.barcodescanner.presentation.intent.createActionOpenDocumentIntent
import com.devappspros.barcodescanner.presentation.intent.createStartActivityIntent
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDatabaseBarcodeViewModel
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBarcodeAnalysisActivityDevApps
import com.devappspros.barcodescanner.presentation.views.activities.DevAppsBaseActivity
import com.devappspros.barcodescanner.presentation.views.activities.MainActivityDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.BaseFragment
import com.devappspros.barcodescanner.presentation.views.recyclerView.history.BarcodeHistoryItemAdapter
import com.devappspros.barcodescanner.presentation.views.recyclerView.history.BarcodeHistoryItemTouchHelperListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date

/**
 * A simple [Fragment] subclass.
 */
class DevAppsMainBarcodeHistoryFragment : BaseFragment(), BarcodeHistoryItemAdapter.OnBarcodeItemListener, BarcodeHistoryItemTouchHelperListener {

    private val devAppsDatabaseBarcodeViewModel: DevAppsDatabaseBarcodeViewModel by activityViewModel()
    private val adapter: BarcodeHistoryItemAdapter = BarcodeHistoryItemAdapter(this)

    private var _binding: FragmentMainHistoryBinding? = null
    private val viewBinding get() = _binding!!

    private var barcodes: List<Barcode>? = null

    private var alertDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainHistoryBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureExportation()
        configureImportation()

        configureMenu()

        viewBinding.fragmentMainHistoryRecyclerView.visibility = View.GONE
        viewBinding.fragmentMainHistoryEmptyTextView.visibility = View.GONE

        configureRecyclerView()

        devAppsDatabaseBarcodeViewModel.barcodeList.observe(viewLifecycleOwner) {
            barcodeItemsSelected.clear()
            barcodes = it
            adapter.updateData(it)

            if (it.isEmpty()) {
                viewBinding.fragmentMainHistoryEmptyTextView.visibility = View.VISIBLE
                viewBinding.fragmentMainHistoryRecyclerView.visibility = View.GONE
            } else {
                viewBinding.fragmentMainHistoryEmptyTextView.visibility = View.GONE
                viewBinding.fragmentMainHistoryRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity: Activity = requireActivity()
        if(activity is DevAppsBaseActivity){
            activity.lockDeviceRotation(false)
        }
    }

    private fun configureMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_history, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId){
                R.id.menu_history_delete_all -> {
                    if(barcodeItemsSelected.isEmpty()){
                        showDeleteAllConfirmationDialog()
                    } else {
                        showDeleteSelectedItemsConfirmationDialog()
                    }
                    true
                }
                R.id.menu_history_export_as_csv -> { startExportation(DevAppsFileFormat.CSV);true }
                R.id.menu_history_export_as_json -> { startExportation(DevAppsFileFormat.JSON);true }
                R.id.menu_history_import_json -> { startImportation(DevAppsFileFormat.JSON);true }
                else -> true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun configureRecyclerView() {
        val recyclerView = viewBinding.fragmentMainHistoryRecyclerView

        val layoutManager = LinearLayoutManager(requireContext())
        val decoration = DevAppsMarginItemDecoration(resources.getDimensionPixelSize(R.dimen.normal_margin))

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(decoration)

        val itemTouchHelperCallback =
            DevAppsCustomItemTouchHelperCallback(
                this,
                0,
                ItemTouchHelper.START// support Rtl
            )
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun startBarcodeAnalysisActivity(barcode: Barcode) {
        val intent = createStartActivityIntent(requireContext(), DevAppsBarcodeAnalysisActivityDevApps::class).apply {
            putExtra(BARCODE_KEY, barcode)
        }
        startActivity(intent)
    }

    // ---- HistoryItemAdapter.OnItemClickListener Implementation ----

    private val barcodeItemsSelected by lazy { mutableListOf<Barcode>() }

    override fun onItemClick(view: View?, barcode: Barcode) {
        startBarcodeAnalysisActivity(barcode)
    }

    override fun onItemSelect(view: View?, barcode: Barcode, isSelected: Boolean) {
        if(isSelected){
            barcodeItemsSelected.add(barcode)
        }else{
            barcodeItemsSelected.remove(barcode)
        }
    }

    override fun isSelectedMode(): Boolean = barcodeItemsSelected.isNotEmpty()

    // ---- HistoryItemTouchHelperListener Implementation ----
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val barcode: Barcode = adapter.getBarcode(position)

        devAppsDatabaseBarcodeViewModel.deleteBarcode(barcode)

        // Dans le cas de texte trop long et/ou contenant des '\n', on adapte la chaine de caractères
        val content = if (barcode.contents.length <= 16) {
            barcode.contents.substringBefore('\n')
        } else
            "${barcode.contents.substring(0, 16).substringBefore('\n')}..."

        showSnackbar(
            text = getString(R.string.snack_bar_message_item_deleted, content),
            actionText = getString(R.string.cancel_label),
            action = {
                devAppsDatabaseBarcodeViewModel.insertBarcode(barcode, true)
            }
        )
    }

    // ---- Delete History ----

    private fun showDeleteAllConfirmationDialog() {
        showDeleteConfirmationDialog(R.string.popup_message_confirmation_delete_history) {
            devAppsDatabaseBarcodeViewModel.deleteAll()
        }
    }

    private fun showDeleteSelectedItemsConfirmationDialog() {
        showDeleteConfirmationDialog(R.string.popup_message_confirmation_delete_selected_items_history) {
            val barcodesDeleted: List<Barcode> = barcodeItemsSelected.toList()
            devAppsDatabaseBarcodeViewModel.deleteBarcodes(barcodesDeleted)
            showSnackbar(
                text = getString(R.string.snack_bar_message_items_deleted),
                actionText = getString(R.string.cancel_label),
                action = {
                    devAppsDatabaseBarcodeViewModel.insertBarcodes(barcodesDeleted)
                }
            )
        }
    }

    private inline fun showDeleteConfirmationDialog(messageRes: Int, crossinline positiveAction: () -> Unit) {
        alertDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.delete_label)
            .setMessage(messageRes)
            .setPositiveButton(R.string.delete_label) { _, _ ->
                positiveAction()
            }
            .setNegativeButton(R.string.cancel_label, null)
            .show()
    }

    // ---- Export ----

    private var exportation: ActivityResultLauncher<Intent>? = null
    private var devAppsFileFormat: DevAppsFileFormat = DevAppsFileFormat.CSV

    private fun configureExportation() {
        exportation = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val uri = it.data?.data
            if(uri != null) {
                export(
                    uri = uri,
                    devAppsFileFormat = devAppsFileFormat
                )
            }
        }
    }

    private fun startExportation(format: DevAppsFileFormat) {
        val date = get<Date>()
        val simpleDateFormat = get<SimpleDateFormat> { parametersOf("yyyy-MM-dd-HH-mm-ss") }
        val dateNameStr = simpleDateFormat.format(date)
        val name = "bs_export_$dateNameStr${format.extension}"

        devAppsFileFormat = format
        val intent: Intent = createActionCreateFileIntent(name, format.mimeType)
        exportation?.launch(intent)
    }

    private fun export(uri: Uri, devAppsFileFormat: DevAppsFileFormat) {
        val barcodesToExport = if(barcodeItemsSelected.isEmpty()) barcodes else barcodeItemsSelected

        barcodesToExport?.let { barcodes ->
            devAppsDatabaseBarcodeViewModel.exportToFile(barcodes, devAppsFileFormat, uri).observe(viewLifecycleOwner) {
                when(it) {
                    is Resource.Progress -> {}
                    is Resource.Success -> {
                        when(it.data) {
                            true -> showSnackbar(getString(R.string.snack_bar_message_file_export_success))
                            else -> showSnackbar(getString(R.string.snack_bar_message_file_export_error))
                        }
                        if(barcodeItemsSelected.isNotEmpty()) {
                            barcodeItemsSelected.clear()
                            adapter.unselectAll()
                        }
                    }
                    is Resource.Failure -> showSnackbar(getString(R.string.snack_bar_message_file_export_error))
                }
            }
        }
    }

    // ---- Import ----

    private var importation: ActivityResultLauncher<Intent>? = null

    private fun startImportation(format: DevAppsFileFormat = DevAppsFileFormat.JSON){
        val dbPickerIntent = createActionOpenDocumentIntent(format.mimeType)
        importation?.launch(dbPickerIntent)
    }

    private fun configureImportation() {
        importation = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val uri = it.data?.data
            if(uri != null) {
                import(uri = uri)
            }
        }
    }

    private fun import(uri: Uri) {
        devAppsDatabaseBarcodeViewModel.importFile(uri).observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Progress -> {}
                is Resource.Success -> {
                    when(it.data) {
                        true -> showSnackbar(getString(R.string.snack_bar_message_file_import_success))
                        else -> showSnackbar(getString(R.string.snack_bar_message_file_import_error))
                    }
                }
                is Resource.Failure -> showSnackbar(getString(R.string.snack_bar_message_file_import_error))
            }
        }
    }

    // ---- UI ----
    private fun showSnackbar(text: String) {
        val activity = requireActivity()
        if(activity is MainActivityDevApps) {
            activity.showSnackbar(text)
        }
    }

    private fun showSnackbar(text: String, actionText: String, action: (View) -> Unit) {
        val activity = requireActivity()
        if(activity is MainActivityDevApps) {
            activity.showSnackbar(text, actionText, action)
        }
    }
}