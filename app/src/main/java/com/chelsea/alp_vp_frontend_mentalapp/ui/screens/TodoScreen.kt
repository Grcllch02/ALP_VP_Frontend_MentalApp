import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Definisi Warna
val DarkBackground = Color(0xFF181818)
val SurfaceGray = Color(0xFF2C2C2C)
val PurpleAccent = Color(0xFF5E5CE6)
val MoonYellow = Color(0xFFFFF1A5)
val TextWhite = Color.White
val TextGray = Color.Gray

// Data Model Dummy
data class TaskItem(
    val time: String,
    val title: String,
    val description: String,
    val categoryColor: Color,
    val duration: String
)

@Composable
fun TodoScreen() {
    val tasks = listOf(
        TaskItem("08:00", "Morning Focus", "Meditasi & Planning", MoonYellow, "30m"),
        TaskItem("09:30", "Design Review", "Review UI Mockups dengan tim", PurpleAccent, "1h"),
        TaskItem("11:00", "Coding Session", "Implementasi Jetpack Compose", PurpleAccent, "2h"),
        TaskItem("14:00", "Lunch Break", "Istirahat sejenak", SurfaceGray, "45m"),
        TaskItem("15:00", "Client Meeting", "Presentasi progress mingguan", MoonYellow, "1h"),
        TaskItem("16:30", "Documentation", "Update user manual", PurpleAccent, "45m"),
        TaskItem("18:00", "Gym Time", "Latihan kaki dan cardio", MoonYellow, "1h")
    )

    Scaffold(
        containerColor = DarkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = PurpleAccent,
                contentColor = TextWhite,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TopBarSection()

            Spacer(modifier = Modifier.height(24.dp))

            DateStrip()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Today's Tasks",
                color = TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tasks) { task ->
                    TaskCard(task)
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun TopBarSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = TextWhite,
            modifier = Modifier.clickable { }
        )
        Text(
            text = "Schedule",
            color = TextWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Menu",
            tint = TextWhite,
            modifier = Modifier.clickable { }
        )
    }
}

@Composable
fun DateStrip() {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val dates = listOf("12", "13", "14", "15", "16", "17", "18")
    val selectedIndex = 4

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(days.size) { index ->
            val isSelected = index == selectedIndex
            Column(
                modifier = Modifier
                    .width(60.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isSelected) PurpleAccent else SurfaceGray)
                    .clickable { }
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dates[index],
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = days[index],
                    color = if (isSelected) TextWhite.copy(alpha = 0.8f) else TextGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TaskCard(task: TaskItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(50.dp)
                .padding(top = 8.dp)
        ) {
            Text(
                text = task.time,
                color = TextWhite,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Text(
                text = task.duration,
                color = TextGray,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceGray)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                        .background(task.categoryColor)
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = task.title,
                        color = TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        color = TextGray,
                        fontSize = 14.sp,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    TodoScreen()
}