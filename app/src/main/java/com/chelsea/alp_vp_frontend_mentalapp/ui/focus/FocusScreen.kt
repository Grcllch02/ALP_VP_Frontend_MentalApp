package com.chelsea.alp_vp_frontend_mentalapp.ui.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Divider
import kotlinx.coroutines.flow.update


@Composable
fun FocusScreen(
    viewModel: FocusViewModel
) {
    // ⬇️ PENTING: Collect state di AWAL, sebelum remember variables
    val state by viewModel.uiState.collectAsState()

    var selectedCategory by remember { mutableStateOf(MusicCategory.FOCUS) }
    var currentMusic by remember { mutableStateOf("Deep Focus") }
    var isMusicPlaying by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP SECTION
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 25.dp),
                    text = "Timer Focus",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (state.isRunning) "Stay focused" else "Choose your focus time",
                    color = Color(0xFF9CA3AF),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // TIMER SECTION
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF16213E),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TIMER DISPLAY
                    Text(
                        text = formatTime(state.remainingSeconds),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    // PRESET BUTTONS
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(15, 25, 45).forEach { minute ->
                            Button(
                                onClick = {
                                    viewModel.setMinutes(minute)
                                },
                                enabled = !state.isRunning,  // ← Disable saat timer running
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0F4C75),
                                    disabledContainerColor = Color(0xFF0F4C75).copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = "$minute min",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // START/PAUSE BUTTON
                    Button(
                        onClick = {
                            if (state.isRunning) {
                                viewModel.pauseTimer()
                            } else {
                                viewModel.startTimer()
                            }
                        },
                        enabled = state.remainingSeconds > 0,  // ← Disable jika waktu 0
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0F4C75),
                            disabledContainerColor = Color(0xFF0F4C75).copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (state.isRunning) "Pause Focus" else "Start Focus",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    color = Color(0xFF2A2A4A),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(22.dp))

                // MUSIC SECTION (tetap sama)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Music",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = if (isMusicPlaying)
                            "Enjoy your focus sound"
                        else
                            "Choose music to support your focus",
                        fontSize = 14.sp,
                        color = Color(0xFF9CA3AF),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // NOW PLAYING
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF16213E),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Now Playing",
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF)
                            )
                            Text(
                                text = currentMusic,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = { isMusicPlaying = !isMusicPlaying },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0F4C75)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (isMusicPlaying) "Pause" else "Play")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CATEGORY ROW
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MusicCategory.values().forEach { category ->
                            Button(
                                onClick = { selectedCategory = category },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        if (selectedCategory == category)
                                            Color(0xFF0F4C75)
                                        else
                                            Color(0xFF1A1A2E)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = category.name.lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // MUSIC LIST
                    Column {
                        dummyMusicList
                            .filter { it.category == selectedCategory }
                            .forEach { music ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .background(
                                            Color(0xFF16213E),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(12.dp)
                                        .clickable {
                                            currentMusic = music.title
                                            isMusicPlaying = true
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = music.title,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                    }
                }
            }
        }
    }
}

// Rest of code tetap sama
data class MusicItem(
    val title: String,
    val category: MusicCategory
)

enum class MusicCategory {
    FOCUS, RELAX, SLEEP
}

val dummyMusicList = listOf(
    MusicItem("Deep Focus", MusicCategory.FOCUS),
    MusicItem("Coding Flow", MusicCategory.FOCUS),
    MusicItem("Peaceful Piano", MusicCategory.RELAX),
    MusicItem("Ocean Wave", MusicCategory.RELAX),
    MusicItem("Night Calm", MusicCategory.SLEEP),
)

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remaining = seconds % 60
    return String.format("%02d:%02d", minutes, remaining)
}
