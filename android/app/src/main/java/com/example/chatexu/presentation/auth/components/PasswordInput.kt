package com.example.chatexu.presentation.auth.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun PasswordInput(
    password: MutableState<TextFieldValue>,
    passwordMaxLength: Int,
    trimInput: (TextFieldValue, Int) -> TextFieldValue,
    modifier: Modifier = Modifier,
) {

    val visibility = remember { mutableStateOf(false) }

    TextField(
        modifier = modifier,
        value = password.value,
        onValueChange = { password.value = trimInput(it, passwordMaxLength) },
        placeholder = { Text(text = "********") },
        label = { Text(text = "Password") },
        singleLine = true,
        visualTransformation = if (visibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image =
                if (visibility.value) Icons.Default.Visibility
                else Icons.Default.VisibilityOff

            val description = if (visibility.value) "Hide password" else "Show password"

            IconButton(
                onClick = {visibility.value = !visibility.value}){
                Icon(imageVector  = image, description)
            }
        }
    )

}
