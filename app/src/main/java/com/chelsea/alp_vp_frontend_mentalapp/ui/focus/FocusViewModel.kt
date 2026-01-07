package com.chelsea.alp_vp_frontend_mentalapp.ui.focus

import android.util.Log
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

    init {
        Log.d("FocusViewModel", "ViewModel created: ${this.hashCode()}")
    }

    fun setMinutes(minutes: Int) {
        Log.d("FocusViewModel", "=== setMinutes called: $minutes ===")

        // Cancel existing timer
        timerJob?.cancel()
        timerJob = null

        // Update state
        val newSeconds = minutes * 60
        _uiState.value = FocusUiState(
            remainingSeconds = newSeconds,
            isRunning = false
        )

        Log.d("FocusViewModel", "State updated: ${_uiState.value}")
    }

    fun startTimer() {
        Log.d("FocusViewModel", "=== startTimer called ===")
        Log.d("FocusViewModel", "Current remainingSeconds: ${_uiState.value.remainingSeconds}")
        Log.d("FocusViewModel", "Current isRunning: ${_uiState.value.isRunning}")
        Log.d("FocusViewModel", "timerJob exists: ${timerJob != null}")

        // Check if already running
        if (timerJob != null) {
            Log.d("FocusViewModel", "Timer already running, exit")
            return
        }

        // Check if time is available
        if (_uiState.value.remainingSeconds <= 0) {
            Log.d("FocusViewModel", "No time remaining, exit")
            return
        }

        Log.d("FocusViewModel", "Creating timer job...")

        timerJob = viewModelScope.launch {
            Log.d("FocusViewModel", "Inside coroutine, setting isRunning=true")

            _uiState.value = _uiState.value.copy(isRunning = true)

            Log.d("FocusViewModel", "Starting countdown loop")

            var count = 0
            while (_uiState.value.remainingSeconds > 0) {
                count++
                Log.d("FocusViewModel", "Loop iteration $count")

                delay(1000L)

                val currentSeconds = _uiState.value.remainingSeconds
                val newSeconds = currentSeconds - 1

                Log.d("FocusViewModel", "Updating: $currentSeconds -> $newSeconds")

                _uiState.value = _uiState.value.copy(remainingSeconds = newSeconds)

                Log.d("FocusViewModel", "After update: ${_uiState.value.remainingSeconds}")
            }

            Log.d("FocusViewModel", "Timer finished!")
            _uiState.value = _uiState.value.copy(isRunning = false)
            timerJob = null
        }

        Log.d("FocusViewModel", "Timer job created: ${timerJob != null}")
    }

    fun pauseTimer() {
        Log.d("FocusViewModel", "=== pauseTimer called ===")
        timerJob?.cancel()
        timerJob = null
        _uiState.value = _uiState.value.copy(isRunning = false)
        Log.d("FocusViewModel", "Timer paused")
    }

    override fun onCleared() {
        Log.d("FocusViewModel", "ViewModel cleared")
        timerJob?.cancel()
        super.onCleared()
    }
}