@file:Suppress("DEPRECATION")

package mad.project.SleepMonitor.components

import androidx.compose.foundation.text.ClickableText
import android.util.Log
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle

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
        onClick = onClick,
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
