package com.chelsea.alp_vp_frontend_mentalapp.ui.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FocusScreen(
    viewModel: FocusViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

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

            // ======================
            // TOP SECTION (TITLE)
            // ======================
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

                // ======================
                // TIMER + PRESET SECTION
                // ======================
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

                    // TIMER
                    Text(
                        text = formatTime(state.remainingSeconds),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

//                    Spacer(modifier = Modifier.height(16.dp))
                }
                Column (
                    modifier = Modifier
                        .padding(top = 15.dp)
                ){
                    // PRESET BUTTONS
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(15, 25, 45).forEach { minute ->
                            Button(
                                onClick = { viewModel.setMinutes(minute) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0F4C75)
                                )
                            ) {
                                Text(
                                    text = "$minute min",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            if (state.isRunning) {
                                viewModel.pauseTimer()
                            } else {
                                viewModel.startTimer()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                        ,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0F4C75)
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
            }

        }
    }
}

/**
 * Format time mm:ss
 */
private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remaining = seconds % 60
    return String.format("%02d:%02d", minutes, remaining)
}
