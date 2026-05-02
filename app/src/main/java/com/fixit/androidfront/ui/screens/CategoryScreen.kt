package com.fixit.androidfront.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fixit.androidfront.ui.theme.*
import com.fixit.androidfront.ui.viewmodels.CategoryViewModel
import com.fixit.androidfront.ui.viewmodels.CategoryState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryId: String,
    onBack: () -> Unit,
    onAdClick: (String) -> Unit,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val categoryState by categoryViewModel.categoryState.collectAsState()

    LaunchedEffect(categoryId) {
        categoryViewModel.fetchCategoryAds(categoryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    if (categoryState is CategoryState.Success) {
                        val state = categoryState as CategoryState.Success
                        val catName = state.category?.name ?: "Categoría"
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFFEF08A), androidx.compose.foundation.shape.CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getIconForCategory(catName), 
                                    contentDescription = "Icon", 
                                    tint = Color(0xFF854D0E), 
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(catName, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = TextDark)
                                Text("${state.jobPosts.size} anuncios disponibles", fontSize = 12.sp, color = TextGray)
                            }
                        }
                    } else {
                        Text("Cargando...", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
            )
        }
    ) { paddingValues ->
        if (categoryState is CategoryState.Loading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryYellow)
            }
        } else if (categoryState is CategoryState.Error) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = (categoryState as CategoryState.Error).message, color = Color.Red)
            }
        } else if (categoryState is CategoryState.Success) {
            val jobPosts = (categoryState as CategoryState.Success).jobPosts
            if (jobPosts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text(text = "No hay anuncios en esta categoría.", color = TextGray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundGray)
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(jobPosts) { post ->
                        AdCard(
                            title = post.title,
                            author = post.author.name,
                            rating = post.author.profile?.rating?.toString() ?: "N/A",
                            price = post.budget?.toString(),
                            categoryName = post.category?.name,
                            onClick = { onAdClick(post.id) }
                        )
                    }
                }
            }
        }
    }
}
