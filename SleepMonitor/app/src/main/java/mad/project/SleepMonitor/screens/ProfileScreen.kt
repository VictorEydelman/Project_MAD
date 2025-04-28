package mad.project.SleepMonitor.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mad.project.SleepMonitor.ui.common.AppScaffold
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(navController: NavController) {
    var username by remember { mutableStateOf("ivan_ivanov") }
    var name by remember { mutableStateOf("Ivan") }
    var surname by remember { mutableStateOf("Ivanov") }
    var gender by remember { mutableStateOf<Gender?>(null) }
    var birthday by remember { mutableStateOf("") }
    var physicalCondition by remember { mutableStateOf<Preference?>(null) }
    var caffeineUsage by remember { mutableStateOf<Preference?>(null) }
    var alcoholUsage by remember { mutableStateOf<Preference?>(null) }
    var wearableDevice by remember { mutableStateOf<String?>(null) }

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
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Setting",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    SettingCard(title = "My Profile") {
                        NonEditableField(label = "Username", value = username)
                        EditableField(label = "Name", text = name, onTextChange = { name = it })
                        EditableField(label = "Surname", text = surname, onTextChange = { surname = it })
                        GenderField(selectedGender = gender, onGenderSelected = { gender = it })
                        BirthdayField(selectedDate = birthday, onDateSelected = { birthday = it })
                    }

                    SettingCard(title = "My Preferences") {
                        PreferenceField(
                            label = "Physical condition",
                            selectedPreference = physicalCondition,
                            onPreferenceSelected = { physicalCondition = it }
                        )
                        PreferenceField(
                            label = "Caffeine usage",
                            selectedPreference = caffeineUsage,
                            onPreferenceSelected = { caffeineUsage = it }
                        )
                        PreferenceField(
                            label = "Alcohol usage",
                            selectedPreference = alcoholUsage,
                            onPreferenceSelected = { alcoholUsage = it }
                        )
                    }

                    SettingCard(title = "Sleep Settings") {
                        NavigationField(label = "Alarm Settings", onClick = { navController.navigate("alarm_screen") })
                        NavigationField(label = "Bed Time Settings", onClick = { navController.navigate("bedtime_screen") })
                        WearableDeviceField(selectedDevice = wearableDevice, onDeviceSelected = { wearableDevice = it })
                    }

                    Button(
                        onClick = { changeAccount() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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
fun EditableField(label: String, text: String, onTextChange: (String) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
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
                onValueChange = onTextChange,
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
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        IconButton(onClick = { isEditing = !isEditing }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
        }
    }
}

@Composable
fun NonEditableField(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

@Composable
fun GenderField(selectedGender: Gender?, onGenderSelected: (Gender?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { expanded = true }.padding(vertical = 4.dp)
    ) {
        Text(
            text = "Gender",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = selectedGender?.displayName ?: "Not selected",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(end = 8.dp)
        )
        IconButton(onClick = { expanded = true }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Gender", tint = Color.White)
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.background(Color(0xFF2A2A3C))
    ) {
        listOf(Gender.MALE, Gender.FEMALE, null).forEach { genderOption ->
            DropdownMenuItem(
                text = { Text(genderOption?.displayName ?: "Not selected", color = Color.White) },
                onClick = {
                    onGenderSelected(genderOption)
                    expanded = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayField(selectedDate: String, onDateSelected: (String) -> Unit) {
    var openDialog by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val datePickerState = rememberDatePickerState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openDialog = true }
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "Birthday",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = if (selectedDate.isNotEmpty()) selectedDate else "Not selected",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(end = 8.dp)
        )
        IconButton(onClick = { openDialog = true }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
        }
    }

    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = { openDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(dateFormatter.format(Date(millis)))
                    }
                    openDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@Composable
fun PreferenceField(label: String, selectedPreference: Preference?, onPreferenceSelected: (Preference?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { expanded = true }.padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = selectedPreference?.displayName ?: "Not selected",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(end = 8.dp)
        )
        IconButton(onClick = { expanded = true }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Preference", tint = Color.White)
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.background(Color(0xFF2A2A3C))
    ) {
        Preference.values().forEach { preference ->
            DropdownMenuItem(
                text = { Text(preference.displayName, color = Color.White) },
                onClick = {
                    onPreferenceSelected(preference)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun WearableDeviceField(selectedDevice: String?, onDeviceSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val devices = listOf("SmartBand 7")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { expanded = true }.padding(vertical = 4.dp)
    ) {
        Text(
            text = "Wearable device",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = selectedDevice ?: "Not selected",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(end = 8.dp)
        )
        IconButton(onClick = { expanded = true }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Device", tint = Color.White)
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.background(Color(0xFF2A2A3C)),
    ) {
        devices.forEach { device ->
            DropdownMenuItem(
                text = { Text(device, color = Color.White) },
                onClick = {
                    onDeviceSelected(device)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun NavigationField(label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female")
}

enum class Preference(val displayName: String) {
    OnceADay("Once a day"),
    TwiceADay("Twice a day"),
    ThreeTimesADay("Three times a day"),
    OnceEveryTwoDays("Once every two days"),
    TwiceAWeek("Twice a week"),
    ThreeTimesAWeek("Three times a week"),
    Rarely("Rarely"),
    Often("Often"),
    Never("Never")
}

fun changeAccount() {
    // TODO: Реализация выхода
}
