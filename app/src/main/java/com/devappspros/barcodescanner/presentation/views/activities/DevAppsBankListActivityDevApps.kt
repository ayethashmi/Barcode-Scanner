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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.utils.BANK_KEY
import com.devappspros.barcodescanner.databinding.ActivityBankListBinding
import com.devappspros.barcodescanner.domain.entity.bank.Bank
import com.devappspros.barcodescanner.presentation.customView.DevAppsCustomItemTouchHelperCallback
import com.devappspros.barcodescanner.presentation.customView.DevAppsMarginItemDecoration
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDatabaseBankViewModel
import com.devappspros.barcodescanner.presentation.views.recyclerView.bankHistory.BankHistoryItemAdapter
import com.devappspros.barcodescanner.presentation.views.recyclerView.bankHistory.BankHistoryItemTouchHelperListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Liste les données de banque pour simplifier la génération de codes EPC.
 * Est ouverte via un onActivityResult dans le fragment BarcodeFormCreatorQrEpcFragment.
 * Lorsqu'on click sur un item, cela renvoie les informations de Bank via un Intent dans le Fragment appelant.
 */
class DevAppsBankListActivityDevApps : DevAppsBaseActivity(), BankHistoryItemAdapter.OnBankItemListener, BankHistoryItemTouchHelperListener {

    private val viewBinding: ActivityBankListBinding by lazy {
        ActivityBankListBinding.inflate(layoutInflater)
    }
    override val rootView: View get() = viewBinding.root

    private val devAppsDatabaseBankViewModel by viewModel<DevAppsDatabaseBankViewModel>()

    private val adapter: BankHistoryItemAdapter = BankHistoryItemAdapter(this)
    private val bankItemSelected by lazy { mutableListOf<Bank>() }
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.activityBankListEmptyTextView.visibility = View.GONE
        viewBinding.activityBankListHistoryRecyclerView.visibility = View.GONE

        setSupportActionBar(viewBinding.activityBankListActivityLayout.toolbar)
        configureRecyclerView()
        observeDatabase()

        setContentView(rootView)
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        menu.removeItem(R.id.menu_history_export)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_history_delete_all -> {
                if(bankItemSelected.isEmpty())
                    showDeleteAllConfirmationDialog()
                else
                    showDeleteSelectedItemsConfirmationDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureRecyclerView() {
        val recyclerView = viewBinding.activityBankListHistoryRecyclerView

        val layoutManager = LinearLayoutManager(this)
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

    private fun observeDatabase() {
        devAppsDatabaseBankViewModel.bankList.observe(this) {
            bankItemSelected.clear()
            adapter.updateData(it)

            if (it.isEmpty()) {
                viewBinding.activityBankListEmptyTextView.visibility = View.VISIBLE
                viewBinding.activityBankListHistoryRecyclerView.visibility = View.GONE
            } else {
                viewBinding.activityBankListEmptyTextView.visibility = View.GONE
                viewBinding.activityBankListHistoryRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    // ---- Item Actions ----

    override fun onItemClick(view: View?, bank: Bank) {
        val intent = Intent().apply {
            putExtra(BANK_KEY, bank)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onItemSelect(view: View?, bank: Bank, isSelected: Boolean) {
        if(isSelected){
            bankItemSelected.add(bank)
        }else{
            bankItemSelected.remove(bank)
        }
    }

    override fun isSelectedMode(): Boolean = bankItemSelected.isNotEmpty()

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val bank: Bank = adapter.getBank(position)
        devAppsDatabaseBankViewModel.deleteBank(bank)
        showSnackbar(
            text = getString(R.string.menu_item_history_removed_from_history),
            actionText = getString(R.string.cancel_label),
            action = {
                devAppsDatabaseBankViewModel.insertBank(bank)
            }
        )
    }

    // ---- Delete History From Menu ----

    private fun showDeleteAllConfirmationDialog() {
        showDeleteConfirmationDialog(R.string.popup_message_confirmation_delete_history) {
            devAppsDatabaseBankViewModel.deleteAll()
        }
    }

    private fun showDeleteSelectedItemsConfirmationDialog() {
        showDeleteConfirmationDialog(R.string.popup_message_confirmation_delete_selected_items_history) {
            val banksDeleted: List<Bank> = bankItemSelected.toList()
            devAppsDatabaseBankViewModel.deleteBanks(banksDeleted)
            showSnackbar(
                text = getString(R.string.snack_bar_message_items_deleted),
                actionText = getString(R.string.cancel_label),
                action = {
                    devAppsDatabaseBankViewModel.insertBanks(banksDeleted)
                }
            )
        }
    }

    private inline fun showDeleteConfirmationDialog(messageRes: Int, crossinline positiveAction: () -> Unit) {
        alertDialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_label)
            .setMessage(messageRes)
            .setPositiveButton(R.string.delete_label) { _, _ ->
                positiveAction()
            }
            .setNegativeButton(R.string.cancel_label, null)
            .show()
    }
}