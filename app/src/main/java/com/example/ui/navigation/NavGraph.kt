package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import android.app.Application
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ui.screens.MainScreen
import com.example.ui.screens.ActiveChatScreen
import com.example.ui.screens.PhoneLoginScreen
import com.example.ui.screens.OtpVerificationScreen
import com.example.ui.viewmodel.ChatViewModel
import com.example.ui.viewmodel.AuthViewModel
import kotlinx.serialization.Serializable
import androidx.lifecycle.ViewModel

@Serializable
object PhoneLoginRoute

@Serializable
data class OtpVerificationRoute(val verificationId: String)

@Serializable
object MainRoute

@Serializable
data class ChatRoute(val channelId: String, val channelName: String, val isGroup: Boolean)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val chatViewModel: ChatViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = PhoneLoginRoute) {
        composable<PhoneLoginRoute> {
            PhoneLoginScreen(
                viewModel = authViewModel,
                onCodeSent = { verificationId ->
                    navController.navigate(OtpVerificationRoute(verificationId))
                }
            )
        }
        composable<OtpVerificationRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<OtpVerificationRoute>()
            OtpVerificationScreen(
                verificationId = route.verificationId,
                viewModel = authViewModel,
                onAuthSuccess = {
                    navController.navigate(MainRoute) {
                        popUpTo<PhoneLoginRoute> { inclusive = true }
                    }
                }
            )
        }
        composable<MainRoute> {
            MainScreen(
                viewModel = chatViewModel,
                onNavigateToChat = { id, name, isGroup ->
                    navController.navigate(ChatRoute(id, name, isGroup))
                }
            )
        }
        composable<ChatRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChatRoute>()
            ActiveChatScreen(
                viewModel = chatViewModel,
                channelId = route.channelId,
                channelName = route.channelName,
                isGroup = route.isGroup,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
