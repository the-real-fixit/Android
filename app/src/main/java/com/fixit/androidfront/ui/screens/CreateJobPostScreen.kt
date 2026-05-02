package com.fixit.androidfront.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixit.androidfront.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobPostScreen(
    onBack: () -> Unit,
    onPostSuccess: () -> Unit,
    isProvider: Boolean = false // Demo placeholder
) {
    var title by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isProvider) "Ofrecer servicios" else "Publicar Anuncio", fontWeight = FontWeight.Bold, color = TextDark) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryYellow)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryYellow)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = if (isProvider) "Describe los servicios que ofreces para que los clientes puedan contactarte."
                           else "Describe lo que necesitas y publicaremos tu anuncio.",
                    fontSize = 14.sp,
                    color = TextDark,
                    fontWeight = FontWeight.Medium
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    
                    OutlinedTextField(
                        value = title, onValueChange = { title = it },
                        label = { Text("Título del anuncio *") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = categoryId, onValueChange = { categoryId = it },
                        label = { Text("Categoría del servicio *") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = description, onValueChange = { description = it },
                        label = { Text("Descripción detallada *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (!isProvider) {
                        OutlinedTextField(
                            value = budget, onValueChange = { budget = it },
                            label = { Text("Presupuesto estimado (opcional)") },
                            leadingIcon = { Text("Q", modifier = Modifier.padding(start = 12.dp)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    OutlinedTextField(
                        value = location, onValueChange = { location = it },
                        label = { Text("Ubicación") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text("Imágenes de referencia", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { /* TODO: Pick images */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Outlined.Image, contentDescription = "Add image", tint = TextDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Seleccionar Imágenes", color = TextDark)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onBack,
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BackgroundGray, contentColor = TextDark),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cancelar", fontWeight = FontWeight.Bold)
                        }
                        
                        val isEnabled = title.isNotBlank() && description.isNotBlank() && categoryId.isNotBlank()
                        
                        Button(
                            onClick = onPostSuccess,
                            enabled = isEnabled,
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryYellow, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Publicar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
