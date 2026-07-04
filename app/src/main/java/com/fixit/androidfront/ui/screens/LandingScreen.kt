package com.fixit.androidfront.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixit.androidfront.R
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
                        stringResource(R.string.landing_app_title),
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
                        Text(stringResource(R.string.landing_btn_login), color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = onNavigateToRegister) {
                        Text(stringResource(R.string.landing_btn_register), color = Color.Black, fontWeight = FontWeight.Bold)
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
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryYellow)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(stringResource(R.string.landing_search_placeholder)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.home_search_cd)) },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Role Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RoleCard(
                    title = stringResource(R.string.landing_role_hire_title),
                    subtitle = stringResource(R.string.landing_role_hire_subtitle),
                    onClick = onNavigateToRegister
                )
                RoleCard(
                    title = stringResource(R.string.landing_role_work_title),
                    subtitle = stringResource(R.string.landing_role_work_subtitle),
                    onClick = onNavigateToRegister
                )
            }

            // Popular Categories header (no static list — real data lives in HomeScreen)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundGray)
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.landing_popular_categories),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextDark,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                )
                Text(
                    text = stringResource(R.string.landing_subtitle_categories_hint),
                    fontSize = 13.sp,
                    color = TextGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
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
