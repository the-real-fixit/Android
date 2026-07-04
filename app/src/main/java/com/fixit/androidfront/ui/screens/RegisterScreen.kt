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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixit.androidfront.R
import com.fixit.androidfront.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var role by remember { mutableStateOf("") }

    // Step 1
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Step 2
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Step 3
    var hasVehicle by remember { mutableStateOf(false) }
    var canTravel by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
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
            color = TextDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(stringResource(R.string.register_subtitle_prefix), color = TextGray, fontSize = 14.sp)
            Text(
                text = stringResource(R.string.register_subtitle_link),
                color = YellowDark,
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
                                if (i <= step) PrimaryYellow else OutlineGray,
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
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                AnimatedContent(targetState = step, label = "step_animation") { currentStep ->
                    when (currentStep) {
                        1 -> StepOne(email, password, { email = it }, { password = it })
                        2 -> StepTwo(name, phone, role, { name = it }, { phone = it }, { role = it })
                        3 -> StepThree(hasVehicle, canTravel, { hasVehicle = it }, { canTravel = it })
                    }
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
                            Text(stringResource(R.string.register_btn_back), color = TextGray)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    val canAdvance = when (step) {
                        1 -> email.isNotBlank() && password.length >= 6
                        2 -> name.isNotBlank() && role.isNotBlank()
                        else -> true
                    }

                    val isLastStep = (step == 3) || (step == 2 && role == "CLIENT")

                    Button(
                        onClick = {
                            if (isLastStep) onRegisterSuccess() else step++
                        },
                        enabled = canAdvance,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryYellow,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
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
fun StepOne(email: String, pass: String, onEmail: (String) -> Unit, onPass: (String) -> Unit) {
    Column {
        Text(stringResource(R.string.register_step1_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Text(stringResource(R.string.register_step1_subtitle), fontSize = 14.sp, color = TextGray)
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
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
    }
}

@Composable
fun StepTwo(
    name: String, phone: String, role: String,
    onName: (String) -> Unit, onPhone: (String) -> Unit, onRole: (String) -> Unit
) {
    Column {
        Text(stringResource(R.string.register_step2_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name, onValueChange = onName,
            label = { Text(stringResource(R.string.register_label_name)) },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone, onValueChange = onPhone,
            label = { Text(stringResource(R.string.register_label_phone)) },
            leadingIcon = { Icon(Icons.Default.Phone, null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(stringResource(R.string.register_role_prompt), fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RoleOption(
                title = stringResource(R.string.register_role_hire_title),
                desc = stringResource(R.string.register_role_hire_desc),
                icon = Icons.Default.Search,
                selected = role == "CLIENT",
                onClick = { onRole("CLIENT") },
                modifier = Modifier.weight(1f)
            )
            RoleOption(
                title = stringResource(R.string.register_role_work_title),
                desc = stringResource(R.string.register_role_work_desc),
                icon = Icons.Default.Work,
                selected = role == "PROVIDER",
                onClick = { onRole("PROVIDER") },
                modifier = Modifier.weight(1f)
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
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = if (selected) Color(0xFFFEF9C3) else BackgroundGray),
        border = androidx.compose.foundation.BorderStroke(2.dp, if (selected) YellowDark else OutlineGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = if (selected) YellowDark else TextGray, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, color = if (selected) YellowDark else TextDark)
            Text(desc, fontSize = 10.sp, color = TextGray)
        }
    }
}

@Composable
fun StepThree(hasVehicle: Boolean, canTravel: Boolean, onVehicle: (Boolean) -> Unit, onTravel: (Boolean) -> Unit) {
    Column {
        Text(stringResource(R.string.register_step3_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "", onValueChange = {},
            label = { Text(stringResource(R.string.register_label_specialty)) },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

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
                colors = CheckboxDefaults.colors(checkedColor = PrimaryYellow)
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
                colors = CheckboxDefaults.colors(checkedColor = PrimaryYellow)
            )
            Text(stringResource(R.string.register_check_travel), fontSize = 14.sp)
        }
    }
}
