package com.fixit.androidfront.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fixit.androidfront.R
import com.fixit.androidfront.ui.theme.*
import com.fixit.androidfront.ui.viewmodels.ProfileState
import com.fixit.androidfront.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val profileState by profileViewModel.profileState.collectAsState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var userRole by remember { mutableStateOf("CLIENT") }

    LaunchedEffect(Unit) {
        profileViewModel.fetchProfile()
    }

    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            val state = (profileState as ProfileState.Success).profile
            name = state.name
            userRole = state.role
            bio = state.profile.bio ?: ""
            address = listOfNotNull(state.profile.department, state.profile.municipality).joinToString(", ")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile_title), fontWeight = FontWeight.Bold, color = TextDark) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.profile_cd_back),
                            tint = TextDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryYellow)
            )
        }
    ) { paddingValues ->
        when (profileState) {
            is ProfileState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = PrimaryYellow) }
            }
            is ProfileState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { Text(text = (profileState as ProfileState.Error).message, color = Color.Red) }
            }
            else -> {
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
                            text = stringResource(R.string.profile_subtitle),
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

                            // Profile Photo
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(OutlineGray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = stringResource(R.string.profile_cd_photo),
                                        modifier = Modifier.size(40.dp),
                                        tint = TextGray
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        stringResource(R.string.profile_photo_label),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { /* TODO: Pick Image */ },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = PrimaryYellow,
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.UploadFile,
                                            contentDescription = stringResource(R.string.profile_cd_upload),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            stringResource(R.string.profile_btn_pick_photo),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            HorizontalDivider(color = OutlineGray)
                            Spacer(modifier = Modifier.height(24.dp))

                            // Form fields
                            OutlinedTextField(
                                value = name, onValueChange = { name = it },
                                label = { Text(stringResource(R.string.profile_label_name)) },
                                modifier = Modifier.fillMaxWidth(), singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = phone, onValueChange = { phone = it },
                                label = { Text(stringResource(R.string.profile_label_phone)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth(), singleLine = true
                            )

                            if (userRole == "PROVIDER") {
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = categoryId, onValueChange = { categoryId = it },
                                    label = { Text(stringResource(R.string.profile_label_specialty)) },
                                    modifier = Modifier.fillMaxWidth(), singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = address, onValueChange = { address = it },
                                label = { Text(stringResource(R.string.profile_label_address)) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = bio, onValueChange = { bio = it },
                                label = { Text(stringResource(R.string.profile_label_bio)) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 4
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = { /* TODO: Save Profile */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryYellow,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Save,
                                    contentDescription = stringResource(R.string.profile_cd_save),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    stringResource(R.string.profile_btn_save),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
