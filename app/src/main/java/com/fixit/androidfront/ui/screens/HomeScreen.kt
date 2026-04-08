package com.fixit.androidfront.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixit.androidfront.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            HomeTopBar()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                WelcomeBanner(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                PopularCategories()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                HomeTabsAndAds(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun HomeTopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryYellow)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fix it!",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )

            // Minimalist profile / logout section for mobile
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(28.dp),
                    tint = TextDark
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "CERRAR SESIÓN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.clickable { /* TODO */ }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(SurfaceWhite, RoundedCornerShape(8.dp)),
            placeholder = { Text("Buscar servicios, profesionales...", color = TextGray) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search", tint = TextGray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun WelcomeBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Bienvenido Samuel Empleador",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aquí puedes buscar y gestionar tus proyectos.",
                fontSize = 14.sp,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryYellow,
                    contentColor = TextDark
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AddCircleOutline, contentDescription = "Publicar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publicar Anuncio", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

data class CategoryInfo(val title: String, val icon: ImageVector)

@Composable
fun PopularCategories() {
    val categories = listOf(
        CategoryInfo("Albañil", Icons.Outlined.Build),
        CategoryInfo("Carpintería", Icons.Outlined.Handyman),
        CategoryInfo("Clases", Icons.Outlined.School),
        CategoryInfo("Mascotas", Icons.Outlined.Pets),
        CategoryInfo("Electricista", Icons.Outlined.Bolt),
        CategoryInfo("Fotografía", Icons.Outlined.PhotoCamera),
        CategoryInfo("Jardinería", Icons.Outlined.Eco),
        CategoryInfo("Limpieza", Icons.Outlined.Sanitizer),
        CategoryInfo("Mudanzas", Icons.Outlined.LocalShipping)
    )

    Column {
        Text(
            text = "Categorías Populares",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(80.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(2.dp, CircleShape)
                            .clip(CircleShape)
                            .background(PrimaryYellow)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.title,
                            modifier = Modifier.size(32.dp),
                            tint = TextDark
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = category.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTabsAndAds(modifier: Modifier = Modifier) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    Column(modifier = modifier) {
        // Custom Tabs
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            TabItem(
                text = "Buscar Profesionales",
                isSelected = selectedTabIndex == 0,
                modifier = Modifier.clickable { selectedTabIndex = 0 }
            )
            Spacer(modifier = Modifier.width(24.dp))
            TabItem(
                text = "Mis Anuncios Publicados",
                isSelected = selectedTabIndex == 1,
                modifier = Modifier.clickable { selectedTabIndex = 1 }
            )
        }
        
        HorizontalDivider(color = OutlineGray)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Anuncios de Profesionales Disponibles",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        // Mock Ads
        AdCard(
            title = "Reparaciones generales del hogar",
            author = "Juan Pérez",
            rating = "4.8"
        )
        Spacer(modifier = Modifier.height(16.dp))
        AdCard(
            title = "Instalación eléctrica residencial",
            author = "Carlos Gómez",
            rating = "4.9"
        )
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) PrimaryYellow else TextGray
        )
        if (isSelected) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(3.dp)
                    .background(PrimaryYellow)
            )
        }
    }
}

@Composable
fun AdCard(title: String, author: String, rating: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(OutlineGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Image, contentDescription = null, tint = TextGray)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = author, fontSize = 14.sp, color = TextGray)
                }
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = "Rating", tint = PrimaryYellow, modifier = Modifier.size(16.dp))
                    Text(text = rating, fontWeight = FontWeight.Bold, color = TextDark)
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun HomeScreenPreview() {
    AndroidFrontTheme {
        HomeScreen()
    }
}
