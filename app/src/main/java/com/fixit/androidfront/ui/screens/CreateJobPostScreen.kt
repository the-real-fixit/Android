package com.fixit.androidfront.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fixit.androidfront.R
import com.fixit.androidfront.ui.theme.*
import com.fixit.androidfront.ui.viewmodels.CreateJobPostState
import com.fixit.androidfront.ui.viewmodels.CreateJobPostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobPostScreen(
    onBack: () -> Unit,
    onPostSuccess: () -> Unit,
    isProvider: Boolean = false,
    viewModel: CreateJobPostViewModel = viewModel()
) {
    val categories = listOf(
        com.fixit.androidfront.data.Category(id = "c1", name = "Electricista"),
        com.fixit.androidfront.data.Category(id = "c2", name = "Plomería"),
        com.fixit.androidfront.data.Category(id = "c3", name = "Pintura"),
        com.fixit.androidfront.data.Category(id = "c4", name = "Carpintería"),
        com.fixit.androidfront.data.Category(id = "c5", name = "Mudanza"),
        com.fixit.androidfront.data.Category(id = "c6", name = "Jardinería"),
        com.fixit.androidfront.data.Category(id = "c7", name = "Técnico"),
        com.fixit.androidfront.data.Category(id = "c8", name = "Albañil"),
        com.fixit.androidfront.data.Category(id = "c9", name = "Limpieza")
    )
    var categoryExpanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    
    // Location fields
    var department by remember { mutableStateOf("") }
    var municipality by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") } // Calle/Avenida/Depto
    var notes by remember { mutableStateOf("") } // Aclaraciones

    // Images
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 3)
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedImages = uris
        }
    }

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val creationState by viewModel.state.collectAsState()

    LaunchedEffect(creationState) {
        if (creationState is CreateJobPostState.Success) {
            Toast.makeText(context, (creationState as CreateJobPostState.Success).message, Toast.LENGTH_SHORT).show()
            onPostSuccess()
        } else if (creationState is CreateJobPostState.Error) {
            Toast.makeText(context, (creationState as CreateJobPostState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

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

                    val selectedCategoryName = categories.find { it.id == categoryId }?.name ?: ""

                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCategoryName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.create_post_label_category)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        categoryId = category.id
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

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

                    // Location section
                    Text("Ubicación", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = department, onValueChange = { department = it },
                            label = { Text("Departamento") },
                            modifier = Modifier.weight(1f), singleLine = true
                        )
                        OutlinedTextField(
                            value = municipality, onValueChange = { municipality = it },
                            label = { Text("Municipio") },
                            modifier = Modifier.weight(1f), singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = address, onValueChange = { address = it },
                        label = { Text("Calle, Avenida, Zona, Depto") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = notes, onValueChange = { notes = it },
                        label = { Text("Notas / Aclaraciones de ubicación") },
                        modifier = Modifier.fillMaxWidth(), minLines = 2
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        stringResource(R.string.create_post_label_images),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { 
                            multiplePhotoPickerLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Image,
                            contentDescription = stringResource(R.string.create_post_cd_add_image),
                            tint = TextDark
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (selectedImages.isNotEmpty()) "${selectedImages.size} imágenes seleccionadas" else stringResource(R.string.create_post_btn_pick_images), 
                            color = TextDark
                        )
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

                        val isEnabled = title.isNotBlank() && description.isNotBlank() && categoryId.isNotBlank() && creationState !is CreateJobPostState.Loading

                        Button(
                            onClick = {
                                viewModel.createJobPost(
                                    title = title,
                                    description = description,
                                    budget = budget,
                                    department = department,
                                    municipality = municipality,
                                    address = address,
                                    notes = notes,
                                    categoryId = categoryId,
                                    imageUris = selectedImages
                                )
                            },
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
                            if (creationState is CreateJobPostState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(stringResource(R.string.create_post_btn_publish), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
