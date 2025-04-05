package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.myapplication.components.*

@Composable
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp, 70.dp, 40.dp, 35.dp)
    ){
        Column(modifier = Modifier.fillMaxSize()) {
            TitleTextComponent(value = "Sign Up")
            NormalTextComponent(value = "Let’s get started to better your sleep")

            NormalTextComponent(value = "Username")
            TextFieldComponent()
            Spacer(modifier = Modifier.height(24.dp))
            NormalTextComponent(value = "Password")
            PasswordFieldComponent()

            Spacer(modifier = Modifier.height(25.dp))
            ClickableLoginTextComponent(
                initialText = "ALREADY HAVE AN ACCOUNT? ",
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen(){
}