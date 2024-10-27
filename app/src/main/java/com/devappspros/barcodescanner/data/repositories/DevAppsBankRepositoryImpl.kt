package com.devappspros.barcodescanner.data.repositories

import androidx.lifecycle.LiveData
import com.devappspros.barcodescanner.data.database.BankDao
import com.devappspros.barcodescanner.domain.entity.bank.Bank
import com.devappspros.barcodescanner.domain.repositories.DevAppsBankRepository

class DevAppsBankRepositoryImpl(private val bankDao: BankDao): DevAppsBankRepository {

    override fun getBankList(): LiveData<List<Bank>> = bankDao.getBankList()

    override suspend fun insertBank(bank: Bank): Long = bankDao.insert(bank)

    override suspend fun insertBanks(banks: List<Bank>) = bankDao.insert(banks)

    override suspend fun deleteAllBank(): Int = bankDao.deleteAll()

    override suspend fun deleteBanks(banks: List<Bank>): Int = bankDao.deleteBanks(banks)

    override suspend fun deleteBank(bank: Bank): Int = bankDao.delete(bank)
}