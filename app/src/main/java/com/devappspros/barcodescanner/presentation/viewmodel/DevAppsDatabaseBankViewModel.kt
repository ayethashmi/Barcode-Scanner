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

package com.devappspros.barcodescanner.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devappspros.barcodescanner.domain.entity.bank.Bank
import com.devappspros.barcodescanner.domain.usecases.DevAppsDatabaseBankUseCase
import kotlinx.coroutines.launch

class DevAppsDatabaseBankViewModel(private val devAppsDatabaseBankUseCase: DevAppsDatabaseBankUseCase): ViewModel() {

    val bankList: LiveData<List<Bank>> = devAppsDatabaseBankUseCase.bankList

    fun insertBank(bank: Bank) = viewModelScope.launch {
        devAppsDatabaseBankUseCase.insertBank(bank)
    }

    fun insertBanks(banks: List<Bank>) = viewModelScope.launch {
        devAppsDatabaseBankUseCase.insertBanks(banks)
    }

    fun deleteBank(bank: Bank) = viewModelScope.launch {
        devAppsDatabaseBankUseCase.deleteBank(bank)
    }

    fun deleteBanks(banks: List<Bank>) = viewModelScope.launch {
        devAppsDatabaseBankUseCase.deleteBanks(banks)
    }

    fun deleteAll() = viewModelScope.launch {
        devAppsDatabaseBankUseCase.deleteAll()
    }
}