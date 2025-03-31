package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.myapplication.R
import com.example.myapplication.components.*

@Composable
fun SignUpScreen(){
    Surface(
        color = colorResource(id = R.color.background),
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background))
            .padding(40.dp, 70.dp, 40.dp, 35.dp)
    ){
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(30.dp))
            TitleTextComponent(value = "Sign Up")
            NormalTextComponent(value = "Let’s get started to better your sleep")

            Spacer(modifier = Modifier.height(130.dp))
            NormalTextComponent(value = "Username")
            TextFieldComponent()
            Spacer(modifier = Modifier.height(24.dp))
            NormalTextComponent(value = "Password")
            PasswordFieldComponent()

            Spacer(modifier = Modifier.height(100.dp))
            ButtonComponent("Sign Up")
            Spacer(modifier = Modifier.height(25.dp))
            ClickableLoginTextComponent(
                initialText = "ALREADY HAVE AN ACCOUNT? ",
                registerText = "LOG UP",
                onTextSelected = {}
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen(){
    SignUpScreen()
}