package com.devappspros.barcodescanner.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devappspros.barcodescanner.domain.entity.customUrl.CustomUrl

@Dao
interface CustomUrlDao {

    @Query("SELECT * FROM CustomUrl ORDER BY name")
    fun getCustomUrlList(): LiveData<List<CustomUrl>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(customUrl: CustomUrl): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customUrls: List<CustomUrl>)

    @Query("UPDATE CustomUrl SET name = :name, url = :url WHERE id = :id")
    suspend fun update(id: Int, name: String, url: String): Int

    @Query("DELETE FROM CustomUrl")
    suspend fun deleteAll(): Int

    @Delete
    suspend fun deleteCustomUrls(customUrls: List<CustomUrl>): Int

    @Delete
    suspend fun delete(customUrl: CustomUrl): Int
}