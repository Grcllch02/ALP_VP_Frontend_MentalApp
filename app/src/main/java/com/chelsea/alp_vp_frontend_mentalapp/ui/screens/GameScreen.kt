package com.chelsea.alp_vp_frontend_mentalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chelsea.alp_vp_frontend_mentalapp.game.GameColors
import com.chelsea.alp_vp_frontend_mentalapp.game.GameViewModel
import com.chelsea.alp_vp_frontend_mentalapp.data.repository.GameRepository
import kotlin.math.roundToInt

@Composable
fun GameScreen(
    userId: Int? = null,
    viewModel: GameViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(userId = userId, repository = GameRepository()) as T
            }
        }
    )
) {
    val gameState = viewModel.gameState.value
    
    var draggedBlockId by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var boardPosition by remember { mutableStateOf(Offset.Zero) }
    var boardCellSize by remember { mutableStateOf(0f) }
    
    // Auto-save when game is over
    LaunchedEffect(gameState.isGameOver) {
        if (gameState.isGameOver && viewModel.isBackendConnected.value) {
            viewModel.saveGameState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section - Score
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "BLOCK BOOM",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ScoreCard("SCORE", gameState.score)
                    ScoreCard("BEST", gameState.highScore)
                }
                
                // Backend status
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (viewModel.isBackendConnected.value) "🟢 Online" else "🔴 Offline",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            // Middle Section - Game Board
            GameBoard(
                gameState = gameState,
                viewModel = viewModel,
                onBoardPositioned = { position, cellSize ->
                    boardPosition = position
                    boardCellSize = cellSize
                }
            )

            // Bottom Section - Available Blocks
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (gameState.isGameOver) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFE74C3C),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Nice Try!",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Score: ${gameState.score}",
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.resetGame() }) {
                                Text("Try Again", fontSize = 16.sp)
                            }
                        }
                    }
                } else {
                    AvailableBlocks(
                        gameState = gameState,
                        viewModel = viewModel,
                        onDragStart = { blockId, cursorPos ->
                            draggedBlockId = blockId
                            dragOffset = cursorPos
                            viewModel.selectBlock(blockId)
                        },
                        onDrag = { cursorPos ->
                            dragOffset = cursorPos
                            // Calculate which cell the block is over
                            if (boardCellSize > 0) {
                                val relativeX = dragOffset.x - boardPosition.x
                                val relativeY = dragOffset.y - boardPosition.y
                                val col = (relativeX / (boardCellSize + 2f)).toInt()
                                val row = (relativeY / (boardCellSize + 2f)).toInt()
                                if (row in 0..7 && col in 0..7) {
                                    viewModel.updatePreview(row, col)
                                }
                            }
                        },
                        onDragEnd = {
                            if (draggedBlockId != null && boardCellSize > 0) {
                                val relativeX = dragOffset.x - boardPosition.x
                                val relativeY = dragOffset.y - boardPosition.y
                                val col = (relativeX / (boardCellSize + 2f)).toInt()
                                val row = (relativeY / (boardCellSize + 2f)).toInt()
                                if (row in 0..7 && col in 0..7) {
                                    viewModel.placeBlock(row, col)
                                }
                            }
                            draggedBlockId = null
                            dragOffset = Offset.Zero
                            viewModel.deselectBlock()
                        }
                    )
                }
            }
        }
        
        // Dragged block overlay
        if (draggedBlockId != null) {
            val draggedBlock = gameState.availableBlocks.find { it.id == draggedBlockId }
            if (draggedBlock != null) {
                DraggingBlockOverlay(
                    block = draggedBlock,
                    offset = dragOffset
                )
            }
        }
    }
}

@Composable
fun ScoreCard(label: String, value: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = Color(0xFF16213E),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun GameBoard(
    gameState: com.chelsea.alp_vp_frontend_mentalapp.game.GameState,
    viewModel: GameViewModel,
    onBoardPositioned: (Offset, Float) -> Unit
) {
    val cellSize = 40.dp
    val cellSizePx = 40f
    val selectedBlock = gameState.availableBlocks.find { it.id == gameState.selectedBlockId }
    val previewPos = gameState.previewPosition

    Column(
        modifier = Modifier
            .background(
                color = Color(0xFF16213E),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .onGloballyPositioned { coordinates ->
                onBoardPositioned(coordinates.positionInWindow(), cellSizePx)
            }
    ) {
        for (row in gameState.board.indices) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                for (col in gameState.board[row].indices) {
                    val cell = gameState.board[row][col]
                    
                    // Check if this cell should show preview
                    val isPreview = selectedBlock != null && 
                        previewPos != null && 
                        selectedBlock.shape.any { (dc, dr) ->
                            previewPos.first + dr == row && previewPos.second + dc == col
                        }

                    val backgroundColor = when {
                        cell.isOccupied -> Color(GameColors.blockColors[cell.color].toLong())
                        isPreview -> Color(GameColors.blockColors[selectedBlock!!.color].toLong()).copy(alpha = 0.5f)
                        else -> Color(0xFF0F3460)
                    }

                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .background(
                                color = backgroundColor,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun AvailableBlocks(
    gameState: com.chelsea.alp_vp_frontend_mentalapp.game.GameState,
    viewModel: GameViewModel,
    onDragStart: (Int, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF16213E),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        gameState.availableBlocks.forEach { block ->
            if (!block.isUsed) {
                DraggableBlock(
                    block = block,
                    isBeingDragged = gameState.selectedBlockId == block.id,
                    onDragStart = { cursorPos -> onDragStart(block.id, cursorPos) },
                    onDrag = onDrag,
                    onDragEnd = onDragEnd
                )
            } else {
                // Placeholder for used blocks
                Box(modifier = Modifier.size(80.dp))
            }
        }
    }
}

@Composable
fun DraggableBlock(
    block: com.chelsea.alp_vp_frontend_mentalapp.game.Block,
    isBeingDragged: Boolean,
    onDragStart: (Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    var blockPosition by remember { mutableStateOf(Offset.Zero) }
    
    Box(
        modifier = Modifier
            .size(80.dp)
            .onGloballyPositioned { coordinates ->
                blockPosition = coordinates.positionInWindow()
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { touchPos ->
                        // Get absolute screen position of cursor
                        val cursorPos = blockPosition + touchPos
                        onDragStart(cursorPos)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        // Get absolute screen position of cursor
                        val cursorPos = blockPosition + change.position
                        onDrag(cursorPos)
                    },
                    onDragEnd = {
                        onDragEnd()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (!isBeingDragged) {
            BlockShape(block)
        }
    }
}

@Composable
fun DraggingBlockOverlay(
    block: com.chelsea.alp_vp_frontend_mentalapp.game.Block,
    offset: Offset
) {
    val density = LocalDensity.current
    val blockSizePx = with(density) { 80.dp.toPx() }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {},  // Consume touches so they don't affect board
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (offset.x - blockSizePx / 2).roundToInt(),
                        (offset.y - blockSizePx / 2).roundToInt()
                    )
                }
                .size(80.dp)
                .zIndex(10f),
            contentAlignment = Alignment.Center
        ) {
            BlockShape(block, alpha = 0.95f)
        }
    }
}

@Composable
fun BlockShape(
    block: com.chelsea.alp_vp_frontend_mentalapp.game.Block,
    alpha: Float = 1f
) {
    val cellSize = 15.dp
    val maxDim = block.shape.maxOfOrNull { (dc, dr) -> maxOf(dc, dr) }?.plus(1) ?: 1
    
    Box(contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0 until maxDim) {
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    for (col in 0 until maxDim) {
                        val isPartOfShape = block.shape.any { (dc, dr) -> dc == col && dr == row }
                        if (isPartOfShape) {
                            Box(
                                modifier = Modifier
                                    .size(cellSize)
                                    .background(
                                        color = Color(GameColors.blockColors[block.color].toLong()).copy(alpha = alpha),
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            )
                        } else {
                            Box(modifier = Modifier.size(cellSize))
                        }
                    }
                }
            }
        }
    }
}
