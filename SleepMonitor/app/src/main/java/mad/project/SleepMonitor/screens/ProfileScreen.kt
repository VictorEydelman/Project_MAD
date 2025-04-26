package mad.project.SleepMonitor.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mad.project.SleepMonitor.ui.common.AppScaffold

@Composable
fun ProfileScreen(navController: NavController) {

    AppScaffold(navController = navController) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF011222))
                .padding(horizontal = 20.dp),
            color = Color(0xFF011222)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Setting",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    SettingCard(title = "My Profile") {
                        EditableField(label = "Name", initialText = "Ivan")
                        EditableField(label = "Surname", initialText = "Ivanov")
                        EditableField(label = "Gender", initialText = "Male")
                        EditableField(label = "Birthday", initialText = "01/01/2004")
                    }

                    SettingCard(title = "My Preferences") {
                        EditableField(label = "Physical condition", initialText = "twice a day")
                        EditableField(label = "Caffeine usage", initialText = "no")
                        EditableField(label = "Alcohol usage", initialText = "no")
                    }

                    SettingCard(title = "Sleep Settings") {
                        EditableField(label = "Alarm Settings", initialText = "")
                        EditableField(label = "Bed Time Settings", initialText = "")
                        EditableField(label = "Wearable device", initialText = "")
                    }

                    Button(
                        onClick = { changeAccount() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        border = BorderStroke(1.dp, Color(0xFF7E73A8)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Change account",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun SettingCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3C3455)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            content()
        }
    }
}

@Composable
fun EditableField(label: String, initialText: String) {
    var text by remember { mutableStateOf(initialText) }
    var isEditing by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        if (isEditing) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.widthIn(min = 100.dp, max = 180.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Cyan,
                    unfocusedIndicatorColor = Color.White
                )
            )
        } else {
            Text(
                text = text,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        IconButton(
            onClick = { isEditing = !isEditing },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.White
            )
        }
    }
}

fun changeAccount() {
    //TODO: Logout
}
