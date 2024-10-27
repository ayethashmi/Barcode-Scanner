package com.devappspros.barcodescanner.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.devappspros.barcodescanner.common.utils.DATABASE_NAME

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS Bank (name TEXT NOT NULL, bic TEXT NOT NULL, iban TEXT NOT NULL PRIMARY KEY)"
        )
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE Barcode ADD COLUMN error_correction_level TEXT NOT NULL DEFAULT 'NONE'"
        )
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS CustomUrl (`id` INTEGER NOT NULL PRIMARY KEY, `name` TEXT NOT NULL, `url` TEXT NOT NULL)"
        )
    }
}


fun createDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
        .build()

fun createBarcodeDao(database: AppDatabase): BarcodeDao = database.barcodeDao()

fun createBankDao(database: AppDatabase): BankDao = database.bankDao()

fun createCustomUrlDao(database: AppDatabase): CustomUrlDao = database.customUrlDao()