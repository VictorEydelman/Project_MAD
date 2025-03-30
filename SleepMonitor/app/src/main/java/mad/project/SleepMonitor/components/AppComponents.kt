package com.example.myapplication.components

import androidx.compose.foundation.text.ClickableText
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import com.example.myapplication.R
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
            color = colorResource(id = R.color.colorText)
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
        color = colorResource(id = R.color.colorText)
    )
}

@Composable
fun TextFieldComponent(){
    val textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = Color.Black,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.Gray
        ),
        keyboardActions = KeyboardActions.Default,
        value = textValue.value,
        onValueChange = {
            textValue.value  = it
        }
    )
}

@Composable
fun PasswordFieldComponent() {
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = Color.Black,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        value = password.value,
        onValueChange = { password.value = it },
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
fun ButtonComponent(Value: String) {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.blue_magenta),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(46.dp)
    ) {
        Text(
            text = "LOGIN",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun ClickableLoginTextComponent(initialText: String, registerText: String, onTextSelected: (String) -> Unit) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White)) {
            append(initialText)
        }
        withStyle(style = SpanStyle(color = colorResource(id = R.color.LoginText))) {
            pushStringAnnotation(tag = registerText, annotation = registerText)
            append(registerText)
        }
    }

    ClickableText(
        text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 18.dp),
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { span ->
                    Log.d("ClickableTextComponent", "${span.item}")
                    if (span.item == registerText) {
                        onTextSelected(span.item)
                    }
                }
        }
    )
}
