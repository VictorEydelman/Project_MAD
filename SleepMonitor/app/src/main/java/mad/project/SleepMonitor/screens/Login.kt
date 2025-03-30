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
fun LoginScreen(){
    Surface(
        color = colorResource(id = R.color.background),
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background))
            .padding(40.dp, 70.dp, 40.dp, 35.dp)
    ){
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(30.dp))
            TitleTextComponent(value = "Login")
            NormalTextComponent(value = "Welcome we’re glad you’re back")

            Spacer(modifier = Modifier.height(130.dp))
            NormalTextComponent(value = "Username")
            TextFieldComponent()
            Spacer(modifier = Modifier.height(24.dp))
            NormalTextComponent(value = "Password")
            PasswordFieldComponent()

            Spacer(modifier = Modifier.height(100.dp))
            ButtonComponent("LoginButton")
            Spacer(modifier = Modifier.height(25.dp))
            ClickableLoginTextComponent(
                initialText = "DON’T HAVE AN ACCOUNT? ",
                registerText = "SIGN UP",
                onTextSelected = {}
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfLoginScreen(){
    LoginScreen()
}