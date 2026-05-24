package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Check
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

@Composable
fun OtpVerificationScreen(
    verificationId: String,
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }

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
                text = "Enter Code",
                color = LoginText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "We have sent an SMS verification code to your device to authorize encryption handshake sequences.",
                color = LoginTextSub,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            TextField(
                value = otpCode,
                onValueChange = { if (it.length <= 6) otpCode = it },
                label = { Text("6-Digit Code", color = LoginTextSub) },
                modifier = Modifier.fillMaxWidth(0.6f),
                textAlign = TextAlign.Center,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LoginSurface,
                    unfocusedContainerColor = LoginSurface,
                    focusedTextColor = LoginText,
                    unfocusedTextColor = LoginText,
                    focusedIndicatorColor = LoginBlue,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            FloatingActionButton(
                onClick = {
                    if (otpCode.length >= 4) {
                        onAuthSuccess()
                    }
                },
                containerColor = LoginBlue,
                contentColor = LoginText,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Check, 
                    contentDescription = "Verify Code"
                )
            }
        }
    }
}
