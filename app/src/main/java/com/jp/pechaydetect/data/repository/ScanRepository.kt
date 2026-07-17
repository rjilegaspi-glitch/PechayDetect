package com.jp.pechaydetect.data.repository

import androidx.lifecycle.LiveData
import com.jp.pechaydetect.data.database.ScanResultDao
import com.jp.pechaydetect.data.model.ScanResult

class ScanRepository(private val dao: ScanResultDao) {

    val allScans: LiveData<List<ScanResult>> = dao.getAllScans()

    fun getRecentScans(limit: Int = 10): LiveData<List<ScanResult>> =
        dao.getRecentScans(limit)

    suspend fun saveScan(scanResult: ScanResult): Long = dao.insert(scanResult)

    suspend fun deleteScan(scanResult: ScanResult) = dao.delete(scanResult)

    suspend fun deleteById(id: Long) = dao.deleteById(id)

    suspend fun getScanById(id: Long): ScanResult? = dao.getScanById(id)

    suspend fun getTotalCount(): Int = dao.getTotalCount()

    suspend fun getCountByClass(className: String): Int = dao.getCountByClass(className)

    suspend fun clearAll() = dao.deleteAll()
}
