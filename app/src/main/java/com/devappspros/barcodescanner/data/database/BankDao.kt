package com.devappspros.barcodescanner.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devappspros.barcodescanner.domain.entity.bank.Bank

@Dao
interface BankDao {

    @Query("SELECT * FROM Bank ORDER BY name")
    fun getBankList(): LiveData<List<Bank>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bank: Bank): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(banks: List<Bank>)

    @Query("DELETE FROM Bank")
    suspend fun deleteAll(): Int

    @Delete
    suspend fun deleteBanks(banks: List<Bank>): Int

    @Delete
    suspend fun delete(bank: Bank): Int
}