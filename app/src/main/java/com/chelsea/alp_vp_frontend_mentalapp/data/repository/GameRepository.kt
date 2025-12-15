package com.chelsea.alp_vp_frontend_mentalapp.data.repository

import com.chelsea.alp_vp_frontend_mentalapp.data.api.ApiService
import com.chelsea.alp_vp_frontend_mentalapp.data.api.RetrofitClient
import com.chelsea.alp_vp_frontend_mentalapp.data.model.CreateGameStateRequest
import com.chelsea.alp_vp_frontend_mentalapp.data.model.GameStateDto
import com.chelsea.alp_vp_frontend_mentalapp.data.model.UpdateGameStateRequest
import com.chelsea.alp_vp_frontend_mentalapp.game.GameState
import com.chelsea.alp_vp_frontend_mentalapp.game.Block
import com.chelsea.alp_vp_frontend_mentalapp.game.Cell
import com.chelsea.alp_vp_frontend_mentalapp.game.BlockShapes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

class GameRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {
    
    /**
     * Check backend health
     */
    suspend fun checkHealth(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getHealth()
            if (response.isSuccessful && response.body()?.status == "ok") {
                Result.Success(true)
            } else {
                Result.Error("Health check failed")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }
    
    /**
     * Save game state to backend
     */
    suspend fun saveGameState(
        userId: Int,
        gameState: GameState
    ): Result<GameStateDto> = withContext(Dispatchers.IO) {
        try {
            val boardStateJson = serializeBoard(gameState.board)
            val nextBlocksJson = serializeAvailableBlocks(gameState.availableBlocks)
            
            val request = CreateGameStateRequest(
                userId = userId,
                boardState = boardStateJson,
                score = gameState.score,
                nextBlocks = nextBlocksJson
            )
            
            val response = apiService.createGameState(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error("Empty response from server")
            } else {
                Result.Error("Failed to save: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Save failed: ${e.message}", e)
        }
    }
    
    /**
     * Update existing game state
     */
    suspend fun updateGameState(
        gameStateId: Int,
        gameState: GameState
    ): Result<GameStateDto> = withContext(Dispatchers.IO) {
        try {
            val boardStateJson = serializeBoard(gameState.board)
            val nextBlocksJson = serializeAvailableBlocks(gameState.availableBlocks)
            
            val request = UpdateGameStateRequest(
                boardState = boardStateJson,
                score = gameState.score,
                nextBlocks = nextBlocksJson
            )
            
            val response = apiService.updateGameState(gameStateId, request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error("Empty response from server")
            } else {
                Result.Error("Failed to update: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Update failed: ${e.message}", e)
        }
    }
    
    /**
     * Load game state from backend
     */
    suspend fun loadGameState(gameStateId: Int): Result<GameState> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getGameState(gameStateId)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    val board = deserializeBoard(dto.boardState)
                    val availableBlocks = deserializeAvailableBlocks(dto.nextBlocks)
                    
                    val gameState = GameState(
                        board = board,
                        score = dto.score,
                        availableBlocks = availableBlocks,
                        selectedBlockId = null,
                        previewPosition = null,
                        isGameOver = false
                    )
                    Result.Success(gameState)
                } ?: Result.Error("Empty response from server")
            } else {
                Result.Error("Failed to load: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Load failed: ${e.message}", e)
        }
    }
    
    /**
     * Get user's latest game state
     */
    suspend fun getUserLatestGameState(userId: Int): Result<GameState?> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserLatestGameState(userId)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    val board = deserializeBoard(dto.boardState)
                    val availableBlocks = deserializeAvailableBlocks(dto.nextBlocks)
                    
                    val gameState = GameState(
                        board = board,
                        score = dto.score,
                        availableBlocks = availableBlocks,
                        selectedBlockId = null,
                        previewPosition = null,
                        isGameOver = false
                    )
                    Result.Success(gameState)
                } ?: Result.Error("Empty response from server")
            } else if (response.code() == 404) {
                Result.Success(null) // No saved game found
            } else {
                Result.Error("Failed to load: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Load failed: ${e.message}", e)
        }
    }
    
    /**
     * Get all game states for a user
     */
    suspend fun getAllGameStates(): Result<List<GameStateDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllGameStates()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error("Failed to fetch: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Fetch failed: ${e.message}", e)
        }
    }
    
    /**
     * Delete game state
     */
    suspend fun deleteGameState(gameStateId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteGameState(gameStateId)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Failed to delete: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Delete failed: ${e.message}", e)
        }
    }
    
    // Serialization helpers
    private fun serializeBoard(board: List<List<Cell>>): String {
        val jsonArray = JSONArray()
        board.forEach { row ->
            val rowArray = JSONArray()
            row.forEach { cell ->
                val cellObj = JSONObject()
                cellObj.put("isOccupied", cell.isOccupied)
                cellObj.put("color", cell.color)
                rowArray.put(cellObj)
            }
            jsonArray.put(rowArray)
        }
        return jsonArray.toString()
    }
    
    private fun deserializeBoard(boardJson: String): List<List<Cell>> {
        val jsonArray = JSONArray(boardJson)
        val board = mutableListOf<List<Cell>>()
        for (i in 0 until jsonArray.length()) {
            val rowArray = jsonArray.getJSONArray(i)
            val row = mutableListOf<Cell>()
            for (j in 0 until rowArray.length()) {
                val cellObj = rowArray.getJSONObject(j)
                val isOccupied = cellObj.getBoolean("isOccupied")
                val color = cellObj.getInt("color")
                row.add(Cell(isOccupied = isOccupied, color = color))
            }
            board.add(row)
        }
        return board
    }
    
    private fun serializeAvailableBlocks(blocks: List<Block>): String {
        val jsonArray = JSONArray()
        blocks.forEach { block ->
            val blockObj = JSONObject()
            blockObj.put("id", block.id)
            blockObj.put("color", block.color)
            blockObj.put("isUsed", block.isUsed)
            // Store shape index to reconstruct later
            blockObj.put("shapeIndex", BlockShapes.allShapes.indexOf(block.shape))
            jsonArray.put(blockObj)
        }
        return jsonArray.toString()
    }
    
    private fun deserializeAvailableBlocks(blocksJson: String): List<Block> {
        val jsonArray = JSONArray(blocksJson)
        val blocks = mutableListOf<Block>()
        for (i in 0 until jsonArray.length()) {
            val blockObj = jsonArray.getJSONObject(i)
            val id = blockObj.getInt("id")
            val color = blockObj.getInt("color")
            val isUsed = blockObj.getBoolean("isUsed")
            val shapeIndex = blockObj.getInt("shapeIndex")
            
            val shape = BlockShapes.allShapes[shapeIndex]
            blocks.add(Block(id = id, shape = shape, color = color, isUsed = isUsed))
        }
        return blocks
    }
}
