package com.chelsea.alp_vp_frontend_mentalapp.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelsea.alp_vp_frontend_mentalapp.data.repository.GameRepository
import com.chelsea.alp_vp_frontend_mentalapp.data.repository.Result
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class GameViewModel(
    private val userId: Int? = null,
    private val repository: GameRepository = GameRepository()
) : ViewModel() {
    private val gameLogic = GameLogic()
    val gameState = mutableStateOf(gameLogic.getGameState())
    
    // Backend state
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val lastSavedGameStateId = mutableStateOf<Int?>(null)
    val isBackendConnected = mutableStateOf(false)

    init {
        checkBackendHealth()
        if (userId != null) {
            loadUserLatestGame(userId)
        } else {
            gameLogic.initializeGame()
            updateState()
        }
    }
    
    /**
     * Load user's latest game or start new game
     */
    private fun loadUserLatestGame(userId: Int) {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getUserLatestGameState(userId)) {
                is Result.Success -> {
                    if (result.data != null) {
                        gameLogic.setGameState(result.data)
                        updateState()
                    } else {
                        // No saved game, start new
                        gameLogic.initializeGame()
                        updateState()
                    }
                }
                is Result.Error -> {
                    // Error loading, start new game
                    gameLogic.initializeGame()
                    updateState()
                }
                else -> {}
            }
            isLoading.value = false
        }
    }
    
    /**
     * Check if backend is reachable
     */
    private fun checkBackendHealth() {
        viewModelScope.launch {
            when (val result = repository.checkHealth()) {
                is Result.Success -> {
                    isBackendConnected.value = true
                }
                is Result.Error -> {
                    isBackendConnected.value = false
                    errorMessage.value = "Backend not connected: ${result.message}"
                }
                else -> {}
            }
        }
    }
    
    /**
     * Save current game state to backend
     */
    fun saveGameState() {
        val currentUserId = userId ?: 1 // Fallback to guest user
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            
            val currentState = gameState.value
            val result = if (lastSavedGameStateId.value != null) {
                repository.updateGameState(lastSavedGameStateId.value!!, currentState)
            } else {
                repository.saveGameState(currentUserId, currentState)
            }
            
            when (result) {
                is Result.Success -> {
                    lastSavedGameStateId.value = result.data.id
                    errorMessage.value = "Game saved successfully!"
                }
                is Result.Error -> {
                    errorMessage.value = "Save failed: ${result.message}"
                }
                else -> {}
            }
            isLoading.value = false
        }
    }
    
    /**
     * Load game state from backend
     */
    fun loadGameState(gameStateId: Int) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            
            when (val result = repository.loadGameState(gameStateId)) {
                is Result.Success -> {
                    gameLogic.setGameState(result.data)
                    updateState()
                    lastSavedGameStateId.value = gameStateId
                    errorMessage.value = "Game loaded successfully!"
                }
                is Result.Error -> {
                    errorMessage.value = "Load failed: ${result.message}"
                }
                else -> {}
            }
            isLoading.value = false
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        errorMessage.value = null
    }

    fun selectBlock(blockId: Int) {
        gameLogic.selectBlock(blockId)
        updateState()
    }

    fun deselectBlock() {
        gameLogic.deselectBlock()
        updateState()
    }

    fun updatePreview(row: Int, col: Int) {
        gameLogic.updatePreview(row, col)
        updateState()
    }

    fun placeBlock(row: Int, col: Int) {
        gameLogic.placeBlock(row, col)
        updateState()
    }

    fun resetGame() {
        gameLogic.resetGame()
        updateState()
    }

    /**
     * Convert cursor pixel coordinates to the nearest grid cell and update the preview.
     *
     * @param x cursor x in pixels
     * @param y cursor y in pixels
     * @param gridLeft left x of the grid in pixels
     * @param gridTop top y of the grid in pixels
     * @param cellSize size (width/height) of a single grid cell in pixels
     * @param rows number of rows in the grid
     * @param cols number of columns in the grid
     */
    fun updatePreviewAtCursor(
        x: Float,
        y: Float,
        gridLeft: Float,
        gridTop: Float,
        cellSize: Float,
        rows: Int,
        cols: Int
    ) {
        val relativeX = (x - gridLeft) / cellSize
        val relativeY = (y - gridTop) / cellSize
        val col = relativeX.roundToInt().coerceIn(0, cols - 1)
        val row = relativeY.roundToInt().coerceIn(0, rows - 1)
        updatePreview(row, col)
    }

    /**
     * Convert cursor pixel coordinates to the nearest grid cell and place the block.
     * Uses the same snapping logic as updatePreviewAtCursor.
     */
    fun placeBlockAtCursor(
        x: Float,
        y: Float,
        gridLeft: Float,
        gridTop: Float,
        cellSize: Float,
        rows: Int,
        cols: Int
    ) {
        val relativeX = (x - gridLeft) / cellSize
        val relativeY = (y - gridTop) / cellSize
        val col = relativeX.roundToInt().coerceIn(0, cols - 1)
        val row = relativeY.roundToInt().coerceIn(0, rows - 1)
        placeBlock(row, col)
    }

    private fun updateState() {
        gameState.value = gameLogic.getGameState()
    }
}
