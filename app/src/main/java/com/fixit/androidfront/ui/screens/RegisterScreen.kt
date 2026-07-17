package com.fixit.androidfront.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixit.androidfront.R
import com.fixit.androidfront.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fixit.androidfront.ui.viewmodels.RegisterViewModel
import com.fixit.androidfront.ui.viewmodels.RegisterState

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
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
    var step by remember { mutableIntStateOf(1) }
    var role by remember { mutableStateOf("") }

    // Step 1
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Step 2
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var phoneCode by remember { mutableStateOf("+502") }

    // Step 3
    var selectedCategoryIds by remember { mutableStateOf(setOf<String>()) }
    var hasVehicle by remember { mutableStateOf(false) }
    var canTravel by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val registerState by registerViewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success) {
            onRegisterSuccess()
            registerViewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Icon(
            imageVector = Icons.Default.PersonAdd,
            contentDescription = stringResource(R.string.register_cd_icon),
            tint = PrimaryYellow,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.register_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(stringResource(R.string.register_subtitle_prefix), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 14.sp)
            Text(
                text = stringResource(R.string.register_subtitle_link),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onBack() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step Indicator
        val totalSteps = if (role == "CLIENT") 2 else 3
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            for (i in 1..totalSteps) {
                key(i) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(if (i <= step) 32.dp else 16.dp)
                            .background(
                                if (i <= step) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 450.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                AnimatedContent(targetState = step, label = "step_animation") { currentStep ->
                    when (currentStep) {
                        1 -> StepOne(
                            email = email,
                            pass = password,
                            confirmPass = confirmPassword,
                            passwordVisible = passwordVisible,
                            confirmPasswordVisible = confirmPasswordVisible,
                            onEmail = { email = it.trim() },
                            onPass = { password = it.trim() },
                            onConfirmPass = { confirmPassword = it.trim() },
                            onTogglePasswordVisible = { passwordVisible = !passwordVisible },
                            onToggleConfirmPasswordVisible = { confirmPasswordVisible = !confirmPasswordVisible }
                        )
                        2 -> StepTwo(name, phone, phoneCode, role, { name = it }, { phone = it.trim() }, { phoneCode = it }, { role = it })
                        3 -> StepThree(hasVehicle, canTravel, categories, selectedCategoryIds, { hasVehicle = it }, { canTravel = it }, {
                            selectedCategoryIds = if (selectedCategoryIds.contains(it)) {
                                selectedCategoryIds - it
                            } else {
                                selectedCategoryIds + it
                            }
                        })
                    }
                }

                if (registerState is RegisterState.Error) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = (registerState as RegisterState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (step > 1) {
                        TextButton(onClick = { step-- }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.register_btn_back),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.register_btn_back), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        }
                    } else {
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    val canAdvance = when (step) {
                        1 -> email.isNotBlank() && password.length >= 6 && password == confirmPassword
                        2 -> name.isNotBlank() && role.isNotBlank()
                        else -> true
                    }

                    val isLastStep = (step == 3) || (step == 2 && role == "CLIENT")

                    val isLoading = registerState is RegisterState.Loading

                    Button(
                        onClick = {
                            if (isLastStep) {
                                registerViewModel.register(email, password, name, role)
                            } else {
                                step++
                            }
                        },
                        enabled = canAdvance && !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            if (isLastStep) stringResource(R.string.register_btn_submit)
                            else stringResource(R.string.register_btn_next),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.register_btn_next),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepOne(
    email: String,
    pass: String,
    confirmPass: String,
    passwordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    onEmail: (String) -> Unit,
    onPass: (String) -> Unit,
    onConfirmPass: (String) -> Unit,
    onTogglePasswordVisible: () -> Unit,
    onToggleConfirmPasswordVisible: () -> Unit
) {
    Column {
        Text(stringResource(R.string.register_step1_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(stringResource(R.string.register_step1_subtitle), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email, onValueChange = onEmail,
            label = { Text(stringResource(R.string.register_label_email)) },
            leadingIcon = { Icon(Icons.Outlined.Email, null) },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = pass, onValueChange = onPass,
            label = { Text(stringResource(R.string.register_label_password)) },
            leadingIcon = { Icon(Icons.Outlined.Lock, null) },
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = onTogglePasswordVisible) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPass, onValueChange = onConfirmPass,
            label = { Text(stringResource(R.string.register_label_confirm_password)) },
            leadingIcon = { Icon(Icons.Outlined.Lock, null) },
            trailingIcon = {
                val icon = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = onToggleConfirmPasswordVisible) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwo(
    name: String, phone: String, phoneCode: String, role: String,
    onName: (String) -> Unit, onPhone: (String) -> Unit, onPhoneCode: (String) -> Unit, onRole: (String) -> Unit
) {
    val phoneCodes = listOf("+502", "+1", "+52", "+503", "+504", "+505", "+506", "+507", "+34")
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(stringResource(R.string.register_step2_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name, onValueChange = onName,
            label = { Text(stringResource(R.string.register_label_name)) },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(0.4f)
            ) {
                OutlinedTextField(
                    value = phoneCode,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    phoneCodes.forEach { code ->
                        DropdownMenuItem(
                            text = { Text(code) },
                            onClick = {
                                onPhoneCode(code)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = phone, onValueChange = onPhone,
                label = { Text(stringResource(R.string.register_label_phone)) },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.weight(0.6f), singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(stringResource(R.string.register_role_prompt), fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RoleOption(
                title = stringResource(R.string.register_role_hire_title),
                desc = stringResource(R.string.register_role_hire_desc),
                icon = Icons.Default.Search,
                selected = role == "CLIENT",
                onClick = { onRole("CLIENT") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            RoleOption(
                title = stringResource(R.string.register_role_work_title),
                desc = stringResource(R.string.register_role_work_desc),
                icon = Icons.Default.Work,
                selected = role == "PROVIDER",
                onClick = { onRole("PROVIDER") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun RoleOption(
    title: String, desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean, onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    val unselectedColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = if (selected) selectedColor else unselectedColor),
        border = androidx.compose.foundation.BorderStroke(2.dp, if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, color = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface)
            Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StepThree(
    hasVehicle: Boolean, 
    canTravel: Boolean, 
    categories: List<com.fixit.androidfront.data.Category>, 
    selectedCategoryIds: Set<String>, 
    onVehicle: (Boolean) -> Unit, 
    onTravel: (Boolean) -> Unit,
    onToggleCategory: (String) -> Unit
) {
    Column {
        Text(stringResource(R.string.register_step3_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.register_label_specialty), fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text("Puedes seleccionar varias especialidades ahora y modificarlas más adelante en tu perfil.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(8.dp))
        
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val selected = selectedCategoryIds.contains(category.id)
                FilterChip(
                    selected = selected,
                    onClick = { onToggleCategory(category.id) },
                    label = { Text(category.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.register_label_mobility), fontSize = 14.sp, fontWeight = FontWeight.Medium)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Checkbox(
                checked = hasVehicle, onCheckedChange = onVehicle,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            Text(stringResource(R.string.register_check_vehicle), fontSize = 14.sp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Checkbox(
                checked = canTravel, onCheckedChange = onTravel,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            Text(stringResource(R.string.register_check_travel), fontSize = 14.sp)
        }
    }
}
