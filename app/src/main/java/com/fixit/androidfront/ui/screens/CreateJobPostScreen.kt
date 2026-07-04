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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixit.androidfront.R
import com.fixit.androidfront.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobPostScreen(
    onBack: () -> Unit,
    onPostSuccess: () -> Unit,
    isProvider: Boolean = false
) {
    var title by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    // Resolve conditional strings once at the top of the composable
    val screenTitle = stringResource(
        if (isProvider) R.string.create_post_title_provider else R.string.create_post_title_client
    )
    val screenSubtitle = stringResource(
        if (isProvider) R.string.create_post_subtitle_provider else R.string.create_post_subtitle_client
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle, fontWeight = FontWeight.Bold, color = TextDark) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.create_post_cd_back),
                            tint = TextDark
                        )
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
                    text = screenSubtitle,
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
                        label = { Text(stringResource(R.string.create_post_label_title)) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = categoryId, onValueChange = { categoryId = it },
                        label = { Text(stringResource(R.string.create_post_label_category)) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = description, onValueChange = { description = it },
                        label = { Text(stringResource(R.string.create_post_label_description)) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isProvider) {
                        OutlinedTextField(
                            value = budget, onValueChange = { budget = it },
                            label = { Text(stringResource(R.string.create_post_label_budget)) },
                            leadingIcon = { Text("Q", modifier = Modifier.padding(start = 12.dp)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    OutlinedTextField(
                        value = location, onValueChange = { location = it },
                        label = { Text(stringResource(R.string.create_post_label_location)) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        stringResource(R.string.create_post_label_images),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { /* TODO: Pick images */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Image,
                            contentDescription = stringResource(R.string.create_post_cd_add_image),
                            tint = TextDark
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.create_post_btn_pick_images), color = TextDark)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onBack,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BackgroundGray,
                                contentColor = TextDark
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(stringResource(R.string.create_post_btn_cancel), fontWeight = FontWeight.Bold)
                        }

                        val isEnabled = title.isNotBlank() && description.isNotBlank() && categoryId.isNotBlank()

                        Button(
                            onClick = onPostSuccess,
                            enabled = isEnabled,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryYellow,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(stringResource(R.string.create_post_btn_publish), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
