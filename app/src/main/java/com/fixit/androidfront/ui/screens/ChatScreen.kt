package com.fixit.androidfront.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixit.androidfront.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBack: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(OutlineGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "User", tint = TextGray, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Juan Pérez", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                            Text("En línea", fontSize = 12.sp, color = TextGray)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryYellow)
            )
        },
        bottomBar = {
            Surface(
                color = SurfaceWhite,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* TODO: Attach File */ }) {
                        Icon(Icons.Default.AttachFile, contentDescription = "Attach", tint = TextGray)
                    }
                    
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Escribe un mensaje...", fontSize = 14.sp) },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = OutlineGray,
                            focusedBorderColor = PrimaryYellow
                        ),
                        maxLines = 4
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = { messageText = "" },
                        modifier = Modifier
                            .background(PrimaryYellow, CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.Black)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            reverseLayout = true
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            
            // Dummy Messages
            item {
                ChatBubble(
                    text = "¡Claro! Puedo llegar a las 3:00 PM con las herramientas necesarias.",
                    isMine = true,
                    time = "10:32 AM"
                )
            }
            item {
                ChatBubble(
                    text = "Perfecto, me parece bien. ¿A qué hora podrías venir?",
                    isMine = false,
                    time = "10:30 AM"
                )
            }
            item {
                ChatBubble(
                    text = "¡Hola! Vi tu anuncio sobre la fuga de agua. Yo te lo puedo arreglar hoy mismo.",
                    isMine = true,
                    time = "10:28 AM"
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Hoy", fontSize = 12.sp, color = TextGray)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(text: String, isMine: Boolean, time: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isMine) PrimaryYellow else SurfaceWhite,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMine) 16.dp else 0.dp,
                        bottomEnd = if (isMine) 0.dp else 16.dp
                    )
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = text,
                color = if (isMine) Color.Black else TextDark,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = time,
            fontSize = 10.sp,
            color = TextGray,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
