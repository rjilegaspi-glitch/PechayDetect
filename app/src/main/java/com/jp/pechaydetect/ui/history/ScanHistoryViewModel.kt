package com.jp.pechaydetect.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jp.pechaydetect.PechayDetectApp
import com.jp.pechaydetect.data.model.ScanResult

class ScanHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as PechayDetectApp).repository

    val allScans: LiveData<List<ScanResult>> = repository.allScans
}
