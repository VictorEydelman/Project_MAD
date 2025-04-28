package mad.project.SleepMonitor.components

import android.os.Build
import androidx.compose.foundation.text.ClickableText
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import mad.project.SleepMonitor.RequestNotificationPermission
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun NormalTextComponent(value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp),
    ) {
        Text(
            text = value,
            modifier = Modifier.align(Alignment.CenterVertically),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal
            ),
            color = Color.White
        )
    }
}
@Composable
fun SubTitleTextComponent(value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = value,
            modifier = Modifier.align(Alignment.CenterVertically),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal
            ),
            color = Color.White
        )
    }
}
@Composable
fun TitleTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth().heightIn(min = 70.dp),
        style = TextStyle(
            fontSize = 48.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal
        ),
        color = Color.White
    )
}

@Composable
fun TextFieldComponent() {
    val textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.Gray
        ),
        keyboardActions = KeyboardActions.Default,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
        },
        textStyle = TextStyle(fontSize = 16.sp)
    )
}

@Composable
fun PasswordFieldComponent() {
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        value = password.value,
        onValueChange = { password.value = it },
        textStyle = TextStyle(fontSize = 16.sp),
        trailingIcon = {
            val icon = if (passwordVisible.value) { Icons.Filled.Visibility
            } else { Icons.Filled.VisibilityOff }
            val description = if (passwordVisible.value) { "Hide password"
            } else { "Show password" }
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = icon, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible.value) { VisualTransformation.None
        } else { PasswordVisualTransformation() }
    )
}

@Composable

fun ButtonComponent(value: String, onClick: () -> Unit) {
    Button(
        onClick = onClick, // добавляем обработчик нажатия
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF313050),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(46.dp)
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ClickableLoginTextComponent(initialText: String, clickableText: String, tag: String, onTextSelected: (String) -> Unit) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White)) { append("$initialText ") }
        pushStringAnnotation(tag = tag, annotation = tag)
        withStyle(style = SpanStyle(color = Color(0xFF9D91FF))) { append(clickableText) }
        pop()
    }
    ClickableText(
        text = annotatedString,
        modifier = Modifier.fillMaxWidth().heightIn(min = 18.dp),
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(start = offset, end = offset).firstOrNull()?.let { span ->
                Log.d("ClickableTextComponent", "Clicked on: ${span.item}")
                onTextSelected(span.item)
            }
        }
    )
}

@Composable
fun TimePicker(onTimeSelected:(String) -> Unit) {
    var selectedTime by remember { mutableStateOf("") }
    var isPermissionGranted by remember { mutableStateOf(false) }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermission { isPermissionGranted = true }
    } else { isPermissionGranted = true }
    if (isPermissionGranted){
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomWheelTimePicker { localTime ->
                val formattedTime = localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                selectedTime = formattedTime
                onTimeSelected(formattedTime)
            }
        }
    }
}

@Composable
fun CustomWheelTimePicker(onTimeSelected: (LocalTime) -> Unit) {
    var selectedHour by remember { mutableIntStateOf(LocalTime.now().hour) }
    var selectedMinute by remember { mutableIntStateOf(LocalTime.now().minute) }
    val hourListState = rememberLazyListState()
    val minuteListState = rememberLazyListState()

    LaunchedEffect(selectedHour) {
        val targetIndex = selectedHour.coerceAtLeast(0)
        hourListState.animateScrollToItem(targetIndex)
    }
    LaunchedEffect(hourListState.isScrollInProgress) {
        if (!hourListState.isScrollInProgress) {
            selectedHour = hourListState.firstVisibleItemIndex.coerceIn(0, 23)
            onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
        }
    }
    LaunchedEffect(selectedMinute) {
        val targetIndex = (selectedMinute).coerceAtLeast(0)
        minuteListState.animateScrollToItem(targetIndex)
    }
    LaunchedEffect(minuteListState.isScrollInProgress) {
        if (!minuteListState.isScrollInProgress) {
            selectedMinute = minuteListState.firstVisibleItemIndex.coerceIn(0, 59)
            onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // hour
        LazyColumn(state = hourListState, modifier = Modifier.width(60.dp).height(128.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            items(26) { index ->
                if (index == 0 || index == 25) {
                    Text(
                        text = "",
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    val hour = index - 1
                    Text(
                        text = hour.toString().padStart(2, '0'),
                        modifier = Modifier.padding(vertical = 8.dp).clickable {
                            selectedHour = hour
                            onTimeSelected(LocalTime.of(hour, selectedMinute))
                        },
                        fontSize = if (selectedHour == hour) 32.sp else 24.sp,
                        color = if (selectedHour == hour) Color.White else Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Text(
            text = ":",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 24.sp,
            color = Color.White
        )
        // minute
        LazyColumn(state = minuteListState, modifier = Modifier.width(64.dp).height(128.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            items(62) {index ->
                if (index == 0 || index == 61) {
                    Text(
                        text = "",
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }
                else{
                    val minute = index - 1
                    Text(
                        text = minute.toString().padStart(2, '0'),
                        modifier = Modifier.padding(vertical = 8.dp).clickable {
                            selectedMinute = minute
                            onTimeSelected(LocalTime.of(selectedHour, minute))
                        },
                        fontSize = if (selectedMinute == minute) 32.sp else 24.sp,
                        color = if (selectedMinute == minute) Color.White else Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SettingSwitch(){
    var checked by remember{ mutableStateOf(true) }
    Switch(checked = checked, onCheckedChange = {checked = it}, modifier = Modifier.semantics{
        contentDescription = "Checker"
    })
}