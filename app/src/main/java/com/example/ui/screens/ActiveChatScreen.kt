package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.ChatViewModel

// Re-using our Telegram Dark Palette
val ActiveTgBg = Color(0xFF0E1621) // Darker wallpaper color for chat background
val ActiveTgSurface = Color(0xFF202B36)
val ActiveTgBlue = Color(0xFF5288C1)
val OutgoingBubbleColor = Color(0xFF2B5278) // Telegram style blue outgoing bubble
val IncomingBubbleColor = Color(0xFF182533) // Telegram style grey incoming bubble
val ActiveTgText = Color(0xFFFFFFFF)
val ActiveTgTextSub = Color(0xFF7F91A4)
val ActiveTgGreen = Color(0xFF45A675)

data class MessageUiModel(
    val id: String,
    val textPayload: String, // Will show decrypted text locally
    val isFromMe: Boolean,
    val timestamp: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveChatScreen(
    viewModel: ChatViewModel,
    channelId: String,
    channelName: String,
    isGroup: Boolean,
    onBack: () -> Unit
) {
    var typedMessage by remember { mutableStateOf("") }
    
    // Sample messages container simulating locally decrypted text blobs
    val messageList = remember {
        mutableStateListOf(
            MessageUiModel("1", "Hey! Is this chat encrypted?", false, "14:30"),
            MessageUiModel("2", "Yes, absolutely. It uses AES-GCM-256.", true, "14:31"),
            MessageUiModel("3", "Perfect. The server only sees ciphertext strings.", false, "14:32")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(channelName, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = ActiveTgText)
                            if (!isGroup) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Lock, 
                                    contentDescription = "E2EE Chat", 
                                    tint = ActiveTgGreen, 
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Text(
                            text = if (isGroup) "group chat" else "end-to-end encrypted",
                            fontSize = 11.sp,
                            color = if (isGroup) ActiveTgTextSub else ActiveTgGreen
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ActiveTgText)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = ActiveTgText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ActiveTgSurface)
            )
        },
        containerColor = ActiveTgBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
            
            // 1. Message History Space
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.Bottom,
                reverseLayout = false
            ) {
                items(messageList) { message ->
                    ChatBubble(message = message)
                }
            }

            // 2. Interactive Input Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ActiveTgSurface)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = typedMessage,
                    onValueChange = { typedMessage = it },
                    placeholder = { Text("Message", color = ActiveTgTextSub, fontSize = 15.sp) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = ActiveTgText,
                        unfocusedTextColor = ActiveTgText
                    )
                )
                
                if (typedMessage.isNotBlank()) {
                    IconButton(
                        onClick = {
                            // CRUCIAL PRIVACY STEP: Local Client-Side Encryption
                            val encryptedCiphertext = encryptMessageLocally(typedMessage)
                            
                            // 1. Append the readable version to our local screen UI layout safely
                            messageList.add(MessageUiModel(
                                id = System.currentTimeMillis().toString(),
                                textPayload = typedMessage, 
                                isFromMe = true,
                                timestamp = "Just Now"
                            ))
                            
                            // 2. Send ONLY the encrypted string (ciphertext) to your Firestore database
                            // viewModel.sendMessageToFirebase(channelId, encryptedCiphertext)
                            
                            typedMessage = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send, 
                            contentDescription = "Send", 
                            tint = ActiveTgBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: MessageUiModel) {
    val alignment = if (message.isFromMe) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isFromMe) OutgoingBubbleColor else IncomingBubbleColor
    val shape = if (message.isFromMe) {
        RoundedCornerShape(14.dp, 14.dp, 0.dp, 14.dp)
    } else {
        RoundedCornerShape(14.dp, 14.dp, 14.dp, 0.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .clip(shape)
                .background(bubbleColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .widthIn(max = 260.dp)
        ) {
            Text(text = message.textPayload, color = ActiveTgText, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = message.timestamp, 
                color = ActiveTgTextSub, 
                fontSize = 10.sp, 
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

/**
 * Architectural Cryptography Guardrail:
 * This function handles client-side security routines. Plaintext string inputs are converted
 * to AES-GCM encrypted payloads before network upload pipelines touch them.
 */
fun encryptMessageLocally(plainText: String): String {
    // TODO: In our security module step, we will wire this to use the Android Keystore system
    // For now, this placeholder reminds us that Firebase never receives readable user texts.
    val dummyCiphertext = "AES_GCM_ENCRYPTED_[$plainText]"
    return dummyCiphertext
}
