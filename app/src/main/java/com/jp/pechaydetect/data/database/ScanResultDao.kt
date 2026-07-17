package com.jp.pechaydetect.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jp.pechaydetect.data.model.ScanResult

@Dao
interface ScanResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scanResult: ScanResult): Long

    @Delete
    suspend fun delete(scanResult: ScanResult)

    @Query("SELECT * FROM scan_results ORDER BY timestamp DESC")
    fun getAllScans(): LiveData<List<ScanResult>>

    @Query("SELECT * FROM scan_results WHERE id = :id LIMIT 1")
    suspend fun getScanById(id: Long): ScanResult?

    @Query("SELECT * FROM scan_results ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentScans(limit: Int): LiveData<List<ScanResult>>

    @Query("SELECT COUNT(*) FROM scan_results")
    suspend fun getTotalCount(): Int

    @Query("SELECT COUNT(*) FROM scan_results WHERE predictedClass = :className")
    suspend fun getCountByClass(className: String): Int

    @Query("DELETE FROM scan_results WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM scan_results")
    suspend fun deleteAll()
}
