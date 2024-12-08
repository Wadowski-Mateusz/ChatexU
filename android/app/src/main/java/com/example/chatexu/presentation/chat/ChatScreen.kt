package com.example.chatexu.presentation.chat

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.common.toMultipartBodyPart
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.MessageType
import com.example.chatexu.presentation.Screen
import com.example.chatexu.presentation.chat.components.MessageLazyList
import com.example.chatexu.presentation.commons.composable.ScreenName
import java.time.Instant

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
    ) {

    val state = viewModel.state.value
    val context = LocalContext.current

    val inputMessage = remember { mutableStateOf(TextFieldValue()) }
    val inputImageUri = remember { mutableStateOf<Uri?>(null) }

    fun sendImage() {
//        val newIcon = iconUri.toMultipartBodyPart(iconUri, context, "image")
        if(inputImageUri.value != null) {
            val uri: Uri = inputImageUri.value!!

            val newIcon = uri.toMultipartBodyPart(uri, context, "image")

            val m = Message(
                messageId = Constants.ID_DEFAULT, // TODO id
                senderId = state.userId,
                chatId = state.chatId,
                timestamp = Instant.MIN,
                messageType = MessageType.Resource(uri.toString()), // Do I even need to send it?
                isEdited = false,
                //            replyTo = answear // TODO
            )

            viewModel.sendImage(m, newIcon)
            inputImageUri.value = null
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {
                inputImageUri.value = it
                sendImage()
            }

        }
    )

    val sendMessage = { answear: String ->
//        Log.d(DebugConstants.TODO, "ChatScreen - Message type + message answear for sending")

        if (inputMessage.value.text.isNotBlank()) {
            val m = Message(
                messageId = Constants.ID_DEFAULT, // TODO id
                senderId = state.userId,
                chatId = state.chatId,
                timestamp = Instant.MIN,
                messageType = MessageType.Text(inputMessage.value.text), //
                isEdited = false,
                replyTo = answear
            )
            viewModel.sendMessage(message = m)
            inputMessage.value = TextFieldValue() // clear input
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {

        Row(verticalAlignment = Alignment.Top) { ScreenName(screenName = "Chat") }

        // Chat info
        if(viewModel.state.value.recipients.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.Top
            ) {

                val chatRow = viewModel.state.value.recipients.first()
                val icon = chatRow.icon!!

                Row(
//                    modifier = Modifier.weight(1.0f)
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 12.dp)
                            .clickable {
                                navController.navigate(Screen.ChatListScreen.route + "/${state.userId}")
                            },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back to chat list"
                    )
                    Image(
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Crop,
                        bitmap = icon.asImageBitmap(),
                        contentDescription = "User icon",
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 8.dp),
                        text = chatRow.nickname,
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp
                    )
                }
            }
        }

        // Messages
        MessageLazyList(
            messages = state.messages, userId = state.userId,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(5.dp)
                .weight(1f)
        )

        // Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Sharp.Add,
                contentDescription = "Send image",
                modifier = Modifier
                    .clickable {
                        selectImage(photoPickerLauncher)
                    }
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
            )

            TextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f)
                ,
                value = inputMessage.value,
                onValueChange = { inputMessage.value = it },
                placeholder = { Text(text = "Enter your message") },
                maxLines = 3,
                trailingIcon = {
                    Icon(
                        Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "Send message",
                        modifier = Modifier
                            .clickable {
                                sendMessage(Constants.ID_DEFAULT)
                            }
                            .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 16.dp)
                    )
                }
            )

        }

    }

}

private fun selectImage(photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {
    photoPickerLauncher.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )
}
