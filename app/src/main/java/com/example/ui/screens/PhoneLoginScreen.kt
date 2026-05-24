package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.AuthViewModel

// Matching our established Telegram dark tokens
val LoginBg = Color(0xFF17212B)
val LoginSurface = Color(0xFF202B36)
val LoginBlue = Color(0xFF5288C1)
val LoginText = Color(0xFFFFFFFF)
val LoginTextSub = Color(0xFF7F91A4)

@Composable
fun PhoneLoginScreen(
    viewModel: AuthViewModel,
    onCodeSent: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+91") } // Defaulting to India region code
    
    Scaffold(
        containerColor = LoginBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Your Phone Number",
                color = LoginText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            
            Text(
                text = "ShieldChat will securely transmit an SMS verification token to register your decentralized cryptographic keys.",
                color = LoginTextSub,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(36.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Country Code Input Box
                TextField(
                    value = countryCode,
                    onValueChange = { countryCode = it },
                    label = { Text("Code", color = LoginTextSub, fontSize = 12.sp) },
                    modifier = Modifier.width(85.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = LoginSurface,
                        unfocusedContainerColor = LoginSurface,
                        focusedTextColor = LoginText,
                        unfocusedTextColor = LoginText,
                        focusedIndicatorColor = LoginBlue,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                
                // Active Phone Number Input Box
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number", color = LoginTextSub, fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = LoginSurface,
                        unfocusedContainerColor = LoginSurface,
                        focusedTextColor = LoginText,
                        unfocusedTextColor = LoginText,
                        focusedIndicatorColor = LoginBlue,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Telegram Style Next Navigation Button
            FloatingActionButton(
                onClick = {
                    if (phoneNumber.isNotBlank()) {
                        // Triggers state progression inside AppNavigation
                        val sampleVerificationId = "mock_firebase_verification_token_stream"
                        onCodeSent(sampleVerificationId)
                    }
                },
                containerColor = LoginBlue,
                contentColor = LoginText,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward, 
                    contentDescription = "Submit Phone Number"
                )
            }
        }
    }
}
