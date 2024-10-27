package com.devappspros.barcodescanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devappspros.barcodescanner.domain.entity.bank.Bank
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.customUrl.CustomUrl

@Database(
    entities = [Barcode::class, Bank::class, CustomUrl::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun barcodeDao(): BarcodeDao

    abstract fun bankDao(): BankDao

    abstract fun customUrlDao(): CustomUrlDao
}