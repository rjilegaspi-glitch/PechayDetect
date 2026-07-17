package com.jp.pechaydetect.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jp.pechaydetect.PechayDetectApp
import com.jp.pechaydetect.data.model.ScanResult
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as PechayDetectApp).repository

    val recentScans: LiveData<List<ScanResult>> = repository.getRecentScans(limit = 5)

    private val _totalScans = MutableLiveData<Int>()
    val totalScans: LiveData<Int> = _totalScans

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _totalScans.value = repository.getTotalCount()
        }
    }
}
