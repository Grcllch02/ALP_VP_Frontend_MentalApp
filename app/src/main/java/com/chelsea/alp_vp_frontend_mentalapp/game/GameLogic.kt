package com.chelsea.alp_vp_frontend_mentalapp.game

import kotlin.random.Random

class GameLogic {
    private var gameState = GameState(
        availableBlocks = generateThreeBlocks()
    )

    fun getGameState(): GameState = gameState
    
    fun setGameState(newState: GameState) {
        gameState = newState
    }

    fun initializeGame() {
        gameState = GameState(
            board = List(8) { List(8) { Cell() } },
            score = 0,
            availableBlocks = generateThreeBlocks()
        )
    }

    fun selectBlock(blockId: Int) {
        gameState = gameState.copy(selectedBlockId = blockId)
    }

    fun deselectBlock() {
        gameState = gameState.copy(selectedBlockId = null, previewPosition = null)
    }

    fun updatePreview(row: Int, col: Int) {
        val selectedBlock = gameState.availableBlocks.find { it.id == gameState.selectedBlockId }
        if (selectedBlock != null && !selectedBlock.isUsed) {
            if (canPlaceBlock(selectedBlock, row, col)) {
                gameState = gameState.copy(previewPosition = row to col)
            } else {
                gameState = gameState.copy(previewPosition = null)
            }
        }
    }

    fun placeBlock(row: Int, col: Int): Boolean {
        if (gameState.isGameOver) return false

        val selectedBlock = gameState.availableBlocks.find { it.id == gameState.selectedBlockId }
            ?: return false

        if (selectedBlock.isUsed) return false

        // Check if block can be placed at this position
        if (!canPlaceBlock(selectedBlock, row, col)) {
            return false
        }

        // Place the block on the board
        val newBoard = gameState.board.map { it.toMutableList() }.toMutableList()
        
        for ((dc, dr) in selectedBlock.shape) {
            val r = row + dr
            val c = col + dc
            if (r in 0..7 && c in 0..7) {
                newBoard[r][c] = Cell(isOccupied = true, color = selectedBlock.color)
            }
        }

        // Mark block as used
        val updatedBlocks = gameState.availableBlocks.map {
            if (it.id == selectedBlock.id) it.copy(isUsed = true) else it
        }

        // Check for complete rows/columns
        val (clearedBoard, clearedLines) = clearCompleteLines(newBoard)
        val blockPoints = selectedBlock.shape.size
        val clearPoints = clearedLines * 10
        val pointsEarned = blockPoints + clearPoints

        // Check if all blocks are used, generate new ones
        val newBlocks = if (updatedBlocks.all { it.isUsed }) {
            generateThreeBlocks()
        } else {
            updatedBlocks
        }

        // Update game state with new board and blocks
        gameState = gameState.copy(
            board = clearedBoard,
            score = gameState.score + pointsEarned,
            highScore = maxOf(gameState.highScore, gameState.score + pointsEarned),
            availableBlocks = newBlocks,
            selectedBlockId = null,
            previewPosition = null
        )

        // Check game over condition immediately after placing block
        // Game is over if none of the remaining blocks can be placed anywhere
        val hasValidMove = newBlocks.any { block -> 
            !block.isUsed && canPlaceBlockAnywhere(block)
        }
        
        if (!hasValidMove) {
            gameState = gameState.copy(isGameOver = true)
        }

        return true
    }

    private fun canPlaceBlock(block: Block, row: Int, col: Int): Boolean {
        for ((dc, dr) in block.shape) {
            val r = row + dr
            val c = col + dc
            if (r !in 0..7 || c !in 0..7) return false
            if (gameState.board[r][c].isOccupied) return false
        }
        return true
    }

    private fun canPlaceBlockAnywhere(block: Block): Boolean {
        if (block.isUsed) return true // Already used blocks don't count
        for (r in 0..7) {
            for (c in 0..7) {
                if (canPlaceBlock(block, r, c)) return true
            }
        }
        return false
    }

    private fun clearCompleteLines(board: List<List<Cell>>): Pair<List<List<Cell>>, Int> {
        var linesCleared = 0
        val newBoard = board.map { it.toMutableList() }.toMutableList()

        // Check and clear complete rows
        for (r in 0..7) {
            if (newBoard[r].all { it.isOccupied }) {
                for (c in 0..7) {
                    newBoard[r][c] = Cell()
                }
                linesCleared++
            }
        }

        // Check and clear complete columns
        for (c in 0..7) {
            if (newBoard.all { it[c].isOccupied }) {
                for (r in 0..7) {
                    newBoard[r][c] = Cell()
                }
                linesCleared++
            }
        }

        return newBoard.map { it.toList() } to linesCleared
    }

    private fun generateThreeBlocks(): List<Block> {
        var blockId = Random.nextInt(10000)
        return List(3) {
            val shape = BlockShapes.allShapes.random()
            val color = Random.nextInt(GameColors.blockColors.size)
            Block(id = blockId++, shape = shape, color = color, isUsed = false)
        }
    }

    fun resetGame() {
        initializeGame()
    }
}
