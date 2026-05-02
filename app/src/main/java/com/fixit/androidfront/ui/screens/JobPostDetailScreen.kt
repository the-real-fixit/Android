package com.fixit.androidfront.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fixit.androidfront.ui.theme.*
import com.fixit.androidfront.ui.viewmodels.JobPostViewModel
import com.fixit.androidfront.ui.viewmodels.JobPostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostDetailScreen(
    jobId: String,
    onBack: () -> Unit,
    jobPostViewModel: JobPostViewModel = viewModel()
) {
    var showOfferDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val jobState by jobPostViewModel.jobPostState.collectAsState()

    LaunchedEffect(jobId) {
        jobPostViewModel.fetchJobPost(jobId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Anuncio", fontWeight = FontWeight.Bold, color = TextDark) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryYellow)
            )
        }
    ) { paddingValues ->
        if (jobState is JobPostState.Loading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryYellow)
            }
        } else if (jobState is JobPostState.Error) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = (jobState as JobPostState.Error).message, color = Color.Red)
            }
        } else if (jobState is JobPostState.Success) {
            val jobPost = (jobState as JobPostState.Success).jobPost

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundGray)
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                
                // Image Placeholder or NetworkImage
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(OutlineGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Job Image", modifier = Modifier.size(64.dp), tint = TextGray)
                }

                // Main Info Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        
                        val categoryName = jobPost.category?.name ?: "General"
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFEF9C3), RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(categoryName, color = Color(0xFF854D0E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = jobPost.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextDark
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val locStr = listOfNotNull(jobPost.municipality, jobPost.department).joinToString(", ").ifEmpty { jobPost.location }
                        if (!locStr.isNullOrEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Color.Red, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(locStr, fontSize = 14.sp, color = TextGray)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        
                        if (jobPost.budget != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFF0FDF4), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Presupuesto: Q${jobPost.budget}", color = Color(0xFF166534), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        HorizontalDivider(color = OutlineGray)
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text("Descripción del Anuncio", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = jobPost.description,
                            fontSize = 16.sp,
                            color = TextGray
                        )
                    }
                }

                // Author Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("AUTOR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(OutlineGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = "User", tint = TextGray)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(jobPost.author.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                Row {
                                    val ratingStr = jobPost.author.profile?.rating?.toString() ?: "0.0"
                                    val jobsCount = jobPost.author.profile?.jobsCompleted ?: 0
                                    Icon(Icons.Default.Star, contentDescription = "Star", tint = PrimaryYellow, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("$ratingStr ($jobsCount trabajos)", fontSize = 12.sp, color = TextGray)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { showOfferDialog = true },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryYellow, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Description, contentDescription = "Offer", modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Enviar Propuesta / Mensaje", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            if (showOfferDialog) {
                AlertDialog(
                    onDismissRequest = { showOfferDialog = false },
                    title = { Text("Propuesta de Trabajo", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text("Para: ${jobPost.author.name}", fontSize = 14.sp, color = TextGray)
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = "", onValueChange = {},
                                label = { Text("Mensaje o Propuesta") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showOfferDialog = false }) {
                            Text("Enviar", color = YellowDark, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showOfferDialog = false }) {
                            Text("Cancelar", color = TextGray)
                        }
                    }
                )
            }
        }
    }
}
