package com.chelsea.alp_vp_frontend_mentalapp.ui.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FocusViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState: StateFlow<FocusUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    /**
     * Set waktu fokus (dalam menit)
     */
    fun setMinutes(minutes: Int) {
        timerJob?.cancel()
        _uiState.value = FocusUiState(
            remainingSeconds = minutes * 60,
            isRunning = false
        )
    }

    /**
     * Start / resume timer
     */
    fun startTimer() {
        if (_uiState.value.remainingSeconds <= 0) return
        if (timerJob != null) return

        _uiState.value = _uiState.value.copy(isRunning = true)

        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    remainingSeconds = _uiState.value.remainingSeconds - 1
                )
            }
            stopTimer()
        }
    }

    /**
     * Pause timer
     */
    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
        _uiState.value = _uiState.value.copy(isRunning = false)
    }

    /**
     * Stop & reset state ketika timer selesai
     */
    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _uiState.value = _uiState.value.copy(isRunning = false)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
