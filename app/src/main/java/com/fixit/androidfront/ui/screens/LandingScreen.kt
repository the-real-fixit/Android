package com.fixit.androidfront.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixit.androidfront.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Fix it!", 
                        fontWeight = FontWeight.Black, 
                        fontSize = 24.sp,
                        color = Color.Black
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryYellow
                ),
                actions = {
                    TextButton(onClick = onNavigateToLogin) {
                        Text("Iniciar Sesión", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = onNavigateToRegister) {
                        Text("Registrarse", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceWhite)
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            // Search Bar (simplified)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryYellow)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Buscar servicios...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = OutlineGray,
                        unfocusedContainerColor = OutlineGray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            // Hero Carousel Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp)
                    .background(Gray900, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Hero Carousel Placeholder\n(Servicios Profesionales)", 
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            // Role Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RoleCard(
                    title = "Quiero contratar",
                    subtitle = "Crea una cuenta",
                    onClick = onNavigateToRegister
                )
                RoleCard(
                    title = "Quiero Trabajar",
                    subtitle = "Crea una cuenta",
                    onClick = onNavigateToRegister
                )
            }

            // Categorías Populares
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundGray)
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = "Categorías Populares",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextDark,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
                
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(5) { index ->
                        Box(
                            modifier = Modifier
                                .size(120.dp, 100.dp)
                                .background(SurfaceWhite, RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Categoría $index", color = TextDark)
                        }
                    }
                }
            }
            
            // Highlighted Ads Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(16.dp)
                    .background(OutlineGray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Highlighted Ads Placeholder", color = TextGray)
            }
        }
    }
}

@Composable
fun RoleCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = OutlineGray),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, color = TextDark)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, fontSize = 12.sp, color = TextGray)
        }
    }
}
