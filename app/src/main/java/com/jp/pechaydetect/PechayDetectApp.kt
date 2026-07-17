package com.jp.pechaydetect

import android.app.Application
import com.jp.pechaydetect.data.database.AppDatabase
import com.jp.pechaydetect.data.repository.ScanRepository

class PechayDetectApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val repository: ScanRepository by lazy { ScanRepository(database.scanResultDao()) }

    override fun onCreate() {
        super.onCreate()
    }
}
