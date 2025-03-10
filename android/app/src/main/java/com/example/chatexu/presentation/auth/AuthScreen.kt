package com.example.chatexu.presentation.auth


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.common.Constants
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.auth.components.FastButton
import com.example.chatexu.presentation.auth.components.PasswordInput

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val login = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val loginMaxLength = 32
    val passwordMaxLength = 24

    fun navigate() {
        navController.navigate(Screen.ChatListScreen.route + "/${state.userId}/${state.jwt}")
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center
    ) {
        val fieldModifierDefault = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .align(Alignment.CenterHorizontally)
//            .border(width = 0.dp, color = Color.Red, shape = RoundedCornerShape(4.dp))

        val fieldModifierBad = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .align(Alignment.CenterHorizontally)
            .border(width = 1.dp, color = Color.Red, shape = RoundedCornerShape(4.dp))

        if(state.loginPage) {

            // Login - user login
            TextField(
                modifier = if (state.badLoginData) fieldModifierBad else fieldModifierDefault,
                value = login.value,
                onValueChange = { login.value = viewModel.trimInput(it, loginMaxLength) },
                placeholder = { Text(text = "login") },
                label = { Text(text = "Login") },
                singleLine = true,
            )

            PasswordInput(
                password = password,
                modifier = if (state.badLoginData) fieldModifierBad else fieldModifierDefault,
                trimInput = viewModel::trimInput,
                passwordMaxLength = passwordMaxLength
            )

            FastButton(
                txt = "Login",
                onClick = {
                    // login logic in LaunchedEffect
                    if (login.value.text.isNotBlank() && password.value.text.isNotBlank())
                        viewModel.login(login.value.text, password.value.text)

                    if(state.error.isNotBlank()) {
                        viewModel.setBadLoginDataAlertVisibility(true)
                    }
                }
            )

            FastButton(
                txt = "Register",
                onClick = {
                    viewModel.showRegister()
                }
            )

            // Developer login screen
//            FastButton(
//                txt = "GO TO DEVELOPER LOGIN",
//                onClick = {
//                    navController.navigate(Screen.AuthScreenDebug.route)
//                }
//            )
        }

        if(state.registerPage) {

            val registerEmail = remember { mutableStateOf(TextFieldValue()) }
            val registerNickname = remember { mutableStateOf(TextFieldValue()) }
            val registerPassword = remember { mutableStateOf(TextFieldValue()) }
            val registerPasswordRepeat = remember { mutableStateOf(TextFieldValue()) }

            TextField(
                modifier = if (state.badRegisterEmail) fieldModifierBad else fieldModifierDefault,
                value = registerEmail.value,
                onValueChange = { registerEmail.value = it },
                placeholder = { Text(text = "email@example.com") },
                label = { Text(text = "email") },
                singleLine = true,
            )

            TextField(
                modifier = if (state.badRegisterNickname) fieldModifierBad else fieldModifierDefault,
                value = registerNickname.value,
                onValueChange = { registerNickname.value = viewModel.trimInput(it, loginMaxLength) },
                placeholder = { Text(text = "nickname") },
                label = { Text(text = "Nickname") },
                singleLine = true,
            )

            PasswordInput(
                password = registerPassword,
                modifier = if (state.badRegisterPasswords) fieldModifierBad else fieldModifierDefault,
                trimInput = viewModel::trimInput,
                passwordMaxLength = passwordMaxLength,
            )

            PasswordInput(
                password = registerPasswordRepeat,
                modifier = if (state.badRegisterPasswords) fieldModifierBad else fieldModifierDefault,
                trimInput = viewModel::trimInput,
                passwordMaxLength = passwordMaxLength,
            )

            FastButton(
                txt = "Register",
                onClick = {
                    if(
                        viewModel.validateRegisterInput(
                            registerNickname.value.text,
                            registerEmail.value.text,
                            registerPassword.value.text,
                            registerPasswordRepeat.value.text
                        )
                    ) {
//                        try {
                            viewModel.registerUser(
                                email = registerEmail.value.text,
                                nickname = registerNickname.value.text,
                                password = registerPassword.value.text
                            )
//                        }
                    }
                }
            )

            FastButton(
                txt = "Go back to login",
                onClick = {
                    viewModel.showLogin()
                }
            )
        }

        LaunchedEffect(state) {
            if (!state.isLoading && state.error.isBlank() && state.registerStatus && state.userId != Constants.ID_DEFAULT && state.jwt != "") {
//                Log.i("REGISTER - SCREEN - true", state.userId)
                navigate()
//                navController.navigate(Screen.ChatListScreen.route + "/${state.userId}")
            } else if (!state.isLoading && state.error.isBlank() && state.loginStatus && state.userId != Constants.ID_DEFAULT && state.jwt != "") {
                navigate()
//                navController.navigate(Screen.ChatListScreen.route + "/${state.userId}" + "/${state.jwt}")
            }

        }


    }


}

