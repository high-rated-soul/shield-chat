package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

// Telegram Palette Mapping
val TgBg = Color(0xFF17212B)
val TgSurface = Color(0xFF202B36)
val TgBlue = Color(0xFF5288C1)
val TgTextMain = Color(0xFFFFFFFF)
val TgTextSub = Color(0xFF7F91A4)
val TgGreen = Color(0xFF45A675)

// Local data structure for compiling UI elements
data class TelegramUiChat(
    val id: String,
    val name: String,
    val snippet: String,
    val timestamp: String,
    val unreadCount: Int,
    val isGroup: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: ChatViewModel,
    onNavigateToChat: (String, String, Boolean) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(0) } // 0 = Chats, 1 = Smart Feed
    var focusModeActive by remember { mutableStateOf(false) }

    // Mock representation of Telegram stream data fed from ViewModel later
    val ongoingChats = remember {
        listOf(
            TelegramUiChat("c1", "Alex W.", "🔒 Cipher keys synchronized on-device.", "14:32", 0, false),
            TelegramUiChat("c2", "Dev Alpha Channel", "New production build push ready to verify.", "14:15", 5, true),
            TelegramUiChat("c3", "Kiran M.", "Are you checking the backend deployment tonight?", "12:04", 1, false),
            TelegramUiChat("c4", "Global Logistics Group", "Corrugated shipments updating tracking schemas.", "10:45", 88, true),
            TelegramUiChat("c5", "Sree", "🔒 Meet up to sign off the database specs.", "Yesterday", 0, false)
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                containerColor = TgSurface,
                modifier = Modifier.width(290.dp)
            ) {
                // Telegram Standard Profile Drawer Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1F2C3A))
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(TgBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("S", color = TgTextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("ShieldChat User", color = TgTextMain, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("@shield_chat_account", color = TgTextSub, fontSize = 13.sp)
                }
                HorizontalDivider(color = TgBg)
                
                // Drawer Options Matrix
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Group, contentDescription = null, tint = TgTextSub) },
                    label = { Text("New Group", color = TgTextMain) },
                    selected = false,
                    onClick = {}
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TgTextSub) },
                    label = { Text("New Secret Chat", color = TgTextMain) },
                    selected = false,
                    onClick = {}
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Campaign, contentDescription = null, tint = TgTextSub) },
                    label = { Text("New Channel", color = TgTextMain) },
                    selected = false,
                    onClick = {}
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null, tint = TgTextSub) },
                    label = { Text("Settings", color = TgTextMain) },
                    selected = false,
                    onClick = {}
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("ShieldChat", fontWeight = FontWeight.SemiBold, color = TgTextMain) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TgTextMain)
                        }
                    },
                    actions = {
                        // Competitive Advantage Element: Focus Mode Switcher
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text(
                                text = "Focus Mode",
                                fontSize = 11.sp,
                                color = if (focusModeActive) TgBlue else TgTextSub,
                                modifier = Modifier.padding(end = 2.dp)
                            )
                            Switch(
                                checked = focusModeActive,
                                onCheckedChange = { focusModeActive = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = TgBlue)
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = TgTextMain)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = TgSurface)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = TgSurface) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Chat, contentDescription = null) },
                        label = { Text("Chats") },
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
                        label = { Text("Smart Feed") },
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 }
                    )
                }
            },
            floatingActionButton = {
                if (currentTab == 0) {
                    FloatingActionButton(
                        containerColor = TgBlue,
                        contentColor = TgTextMain,
                        onClick = {}
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Compose")
                    }
                }
            },
            containerColor = TgBg
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(TgBg)
            ) {
                if (currentTab == 0) {
                    // Filter engine logic driven by Focus Mode state
                    val clearFeed = if (focusModeActive) ongoingChats.filter { !it.isGroup } else ongoingChats
                    
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(clearFeed) { chat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToChat(chat.id, chat.name, chat.isGroup) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(if (chat.isGroup) Color(0xFFE4A652) else TgBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(chat.name.take(1), color = TgTextMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(chat.name, color = TgTextMain, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                                            if (!chat.isGroup) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(Icons.Default.Lock, contentDescription = "E2EE Verified", tint = TgGreen, modifier = Modifier.size(13.dp))
                                            }
                                        }
                                        Text(chat.timestamp, color = TgTextSub, fontSize = 11.sp)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = chat.snippet,
                                            color = TgTextSub,
                                            fontSize = 13.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (chat.unreadCount > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(start = 6.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF4EA4F6))
                                                    .padding(horizontal = 7.dp, vertical = 1.dp)
                                            ) {
                                                Text(chat.unreadCount.toString(), color = TgTextMain, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                            HorizontalDivider(color = Color(0xFF10171D), modifier = Modifier.padding(start = 80.dp))
                        }
                    }
                } else {
                    // Smart Feed Content Container Layout
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = TgSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TgBlue, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("AI Digest Matrix", fontWeight = FontWeight.SemiBold, color = TgTextMain, fontSize = 15.sp)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• Dev Alpha Channel: Local 256-bit AES key-generation routines completed validation schemas.\n\n" +
                                           "• Global Logistics Group: Supply chain manifests for container routing require structural data updates.",
                                    color = TgTextSub,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                               )
                            }
                        }
                    }
                }
            }
        }
    }
}
