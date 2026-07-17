package com.jp.pechaydetect.ui.analysis

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jp.pechaydetect.PechayDetectApp
import com.jp.pechaydetect.data.model.ScanResult
import com.jp.pechaydetect.ml.PechayClassifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnalysisViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as PechayDetectApp).repository
    private val classifier = PechayClassifier(application)

    sealed class AnalysisState {
        object Loading : AnalysisState()
        data class Success(val scanId: Long, val result: PechayClassifier.ClassificationResult) : AnalysisState()
        data class Error(val message: String) : AnalysisState()
    }

    private val _state = MutableLiveData<AnalysisState>(AnalysisState.Loading)
    val state: LiveData<AnalysisState> = _state

    fun analyze(imageUriString: String) {
        viewModelScope.launch {
            _state.value = AnalysisState.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    classifier.initialize()
                    val uri = Uri.parse(imageUriString)
                    classifier.classifyFromUri(uri)
                }
                val scanResult = ScanResult(
                    imagePath = imageUriString,
                    predictedClass = result.predictedClass,
                    confidence = result.confidence,
                    isConfident = result.isConfident
                )
                val savedId = repository.saveScan(scanResult)
                _state.value = AnalysisState.Success(savedId, result)
            } catch (e: Exception) {
                _state.value = AnalysisState.Error(e.message ?: "Analysis failed")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        classifier.close()
    }
}
