package com.example.chatexu.presentation.user_options

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.common.toMultipartBodyPart
import com.example.chatexu.presentation.commons.composable.ScreenName
import com.example.chatexu.presentation.getUserErrorIcon


@Composable
fun UserOptionsScreen(
    navController: NavController,
    viewModel: UserOptionsViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val context = LocalContext.current

    val showNicknameInputError = remember { mutableStateOf(false) }
    val nicknameInput = remember { mutableStateOf(TextFieldValue()) }
    val inputIconUri = remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            // Pick image
            // go again into image picker without discarding nor applying changes
            // click Back button in image picker
            // this action assign `null` to `it`, so earlier picked image is discarded
            // this if prevents it
            if (it != null)
                inputIconUri.value = it
        }
    )


    fun uploadIcon(iconUri: Uri) {
        val newIcon = iconUri.toMultipartBodyPart(iconUri, context, "icon")
        viewModel.updateIcon(newIcon)
    }

    Box {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            Row {
                if (state.error.isNotBlank()) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
            }


            // Debug - screen name
            Row {
                ScreenName(screenName = "USER OPTIONS")
            }

            // Change user icon
            Column {
                val horizontalPadding = 80.dp
                Row {
                    Text(
                        text = "Click on Your profile picture to change it!",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Row {
                    val currentUserIcon: Bitmap = state.currentUser.icon ?: getUserErrorIcon(LocalContext.current)
                    val imageModifier = Modifier
                        .padding(
                            top = 8.dp,
                            bottom = 8.dp,
                            start = horizontalPadding,
                            end = horizontalPadding
                        )
                        .fillMaxWidth()
                        .clickable {
                            Log.d(DebugConstants.PEEK, "Image has been clicked")
                            selectImage(photoPickerLauncher)
                        }

                    if(inputIconUri.value == null) {
                        Image(
                            modifier = imageModifier,
                            bitmap = currentUserIcon.asImageBitmap(),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = "User icon"
                        )
                    }
                    else {
                        Image(
                            modifier = imageModifier,
                            painter = rememberAsyncImagePainter(model = inputIconUri.value),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = "User icon"
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val cutout = 12.dp
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = horizontalPadding),
                        shape = RoundedCornerShape(cutout, 0.dp, 0.dp, cutout),
                        onClick = { inputIconUri.value = null }
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Discard changes", )
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = horizontalPadding),
                        shape = RoundedCornerShape(0.dp, cutout, cutout, 0.dp),
                        onClick = {
                            if(inputIconUri.value != null) {
                                uploadIcon(inputIconUri.value!!)
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Apply changes" )
                    }
                }

            }

            // User nickname change field
            Column(
                modifier = Modifier
                    .background(Color.Gray)
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                // Information
                Row {
                    Text(
                        text = "Change nickname",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Current nickname
                Row {
                    Text(
                        text = state.currentUser.nickname.ifBlank { "Empty nickname" },
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                        )
                    }

                // Change nickname form
                Row {
                    TextField(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 4.dp,
                                color = if (showNicknameInputError.value) Color.Red else Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        label = { Text(text = "New nickname", color = Color.Gray) },
                        shape = RoundedCornerShape(8.dp),
                        value = nicknameInput.value,
                        onValueChange = {
                            nicknameInput.value = it
                            if(isNicknameValid(nicknameInput.value.text)) showNicknameInputError.value = false
                        },
                        singleLine = true,
                        trailingIcon = {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Change nickname",
                                modifier = Modifier
                                    .clickable {
                                        Log.d(DebugConstants.PEEK, "The \"Change nickname\" button has been pressed")
                                        if(!isNicknameValid(nicknameInput.value.text)) {
                                            showNicknameInputError.value = true
                                        } else {
//                                            nicknameInput.value.text.trim()
//                                            Log.d(DebugConstants.TODO, "Nickname is valid, send to backend")
                                            Log.d(DebugConstants.PEEK, "Update nickname click")
                                            viewModel.updateNickname(nicknameInput.value.text)
                                        }
                                    }
                            )
                        }
                    )
                }
            }

        }

    }



}

private fun isNicknameValid (nicknameToValidate: String): Boolean {
    Log.w(DebugConstants.TODO, "check if nickname already in use?")

    val nicknameNoWhitespaces = nicknameToValidate.replace("\\s".toRegex(), "")
    return (nicknameNoWhitespaces.length in (3..16))

}

private fun selectImage(photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {
    photoPickerLauncher.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )
}

