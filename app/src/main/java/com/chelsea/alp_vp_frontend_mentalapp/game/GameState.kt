package com.chelsea.alp_vp_frontend_mentalapp.game

// Game board cell - can be empty or occupied
data class Cell(
    val isOccupied: Boolean = false,
    val color: Int = 0 // Color index for different block types
)

// Block piece data
data class Block(
    val id: Int,
    val shape: List<Pair<Int, Int>>, // Relative coordinates of cells in block
    val color: Int,
    val isUsed: Boolean = false
)

// Main game state
data class GameState(
    val board: List<List<Cell>> = List(8) { List(8) { Cell() } },
    val score: Int = 0,
    val highScore: Int = 0,
    val isGameOver: Boolean = false,
    val availableBlocks: List<Block> = emptyList(), // 3 blocks at bottom
    val selectedBlockId: Int? = null,
    val previewPosition: Pair<Int, Int>? = null // Preview where block will be placed
)

// Block definitions - BlockBlast style shapes
object BlockShapes {
    // Single cells
    val single = listOf(0 to 0)
    
    // 2-cell shapes
    val line2H = listOf(0 to 0, 1 to 0)
    val line2V = listOf(0 to 0, 0 to 1)
    
    // 3-cell shapes
    val line3H = listOf(0 to 0, 1 to 0, 2 to 0)
    val line3V = listOf(0 to 0, 0 to 1, 0 to 2)
    val lShape3 = listOf(0 to 0, 0 to 1, 1 to 1)
    val lShape3Mirror = listOf(1 to 0, 0 to 1, 1 to 1)
    
    // 4-cell shapes
    val line4H = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)
    val line4V = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3)
    val square2x2 = listOf(0 to 0, 1 to 0, 0 to 1, 1 to 1)
    val lShape4 = listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2)
    val lShape4Mirror = listOf(1 to 0, 1 to 1, 0 to 2, 1 to 2)
    val tShape = listOf(0 to 0, 1 to 0, 2 to 0, 1 to 1)
    
    // 5-cell shapes
    val line5H = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0)
    val line5V = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3, 0 to 4)
    val plus = listOf(1 to 0, 0 to 1, 1 to 1, 2 to 1, 1 to 2)
    val lShape5 = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3, 1 to 3)
    
    // 9-cell shape
    val square3x3 = listOf(
        0 to 0, 1 to 0, 2 to 0,
        0 to 1, 1 to 1, 2 to 1,
        0 to 2, 1 to 2, 2 to 2
    )
    
    val allShapes = listOf(
        single, line2H, line2V,
        line3H, line3V, lShape3, lShape3Mirror,
        line4H, line4V, square2x2, lShape4, lShape4Mirror, tShape,
        line5H, line5V, plus, lShape5,
        square3x3
    )
}

// Game colors palette - BlockBlast style
object GameColors {
    val blockColors = listOf(
        0xFF00BCD4, // Cyan
        0xFFFF5722, // Deep Orange
        0xFF4CAF50, // Green
        0xFFFFEB3B, // Yellow
        0xFF9C27B0, // Purple
        0xFFFF9800, // Orange
        0xFF2196F3, // Blue
        0xFFE91E63  // Pink
    )
}
