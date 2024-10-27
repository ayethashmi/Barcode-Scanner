package com.devappspros.barcodescanner.data.repositories

import androidx.lifecycle.LiveData
import com.devappspros.barcodescanner.data.database.BarcodeDao
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeType
import com.devappspros.barcodescanner.domain.repositories.DevAppsBarcodeRepository

class DevAppsBarcodeRepositoryImpl(private val barcodeDao: BarcodeDao): DevAppsBarcodeRepository {

    override fun getBarcodeList(): LiveData<List<Barcode>> = barcodeDao.getBarcodeList()

    override fun getBarcodeByDate(date: Long): LiveData<Barcode?> = barcodeDao.getBarcodeByDate(date)

    override suspend fun isExists(contents: String, format: String): Boolean = barcodeDao.isExists(contents, format)

    override suspend fun insertBarcode(barcode: Barcode): Long = barcodeDao.insert(barcode)

    override suspend fun insertBarcodes(barcodes: List<Barcode>) = barcodeDao.insert(barcodes)

    override suspend fun update(date: Long, contents: String, barcodeType: BarcodeType, name: String): Int = barcodeDao.update(date, contents, barcodeType.name, name)

    override suspend fun updateType(date: Long, barcodeType: BarcodeType): Int = barcodeDao.updateType(date, barcodeType.name)

    override suspend fun updateName(date: Long, name: String): Int = barcodeDao.updateName(date, name)

    override suspend fun updateTypeAndName(date: Long,
                                           barcodeType: BarcodeType,
                                           name: String
    ): Int = barcodeDao.updateTypeAndName(date, barcodeType.name, name)

    override suspend fun deleteAllBarcode(): Int = barcodeDao.deleteAll()

    override suspend fun deleteBarcodes(barcodes: List<Barcode>): Int = barcodeDao.deleteBarcodes(barcodes)

    override suspend fun deleteBarcode(barcode: Barcode): Int = barcodeDao.delete(barcode)
}