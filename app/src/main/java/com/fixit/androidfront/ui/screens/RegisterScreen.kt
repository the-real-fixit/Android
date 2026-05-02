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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            contentDescription = "Register",
            tint = PrimaryYellow,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Crea una cuenta nueva",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextDark
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row {
            Text("O ", color = TextGray, fontSize = 14.sp)
            Text(
                text = "inicia sesión si ya tienes una",
                color = YellowDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onBack() }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Step Indicator
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            for (i in 1..3) {
                if (i == 3 && role == "CLIENT") continue
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
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().widthIn(max = 450.dp),
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Atrás", color = TextGray)
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
                            if (isLastStep) {
                                onRegisterSuccess()
                            } else {
                                step++
                            }
                        },
                        enabled = canAdvance,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryYellow,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isLastStep) "Registrarse" else "Siguiente", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StepOne(email: String, pass: String, onEmail: (String) -> Unit, onPass: (String) -> Unit) {
    Column {
        Text("Datos de acceso", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Text("Ingresa tu correo y crea una contraseña segura.", fontSize = 14.sp, color = TextGray)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = email, onValueChange = onEmail,
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Outlined.Email, null) },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = pass, onValueChange = onPass,
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Outlined.Lock, null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
    }
}

@Composable
fun StepTwo(name: String, phone: String, role: String, onName: (String) -> Unit, onPhone: (String) -> Unit, onRole: (String) -> Unit) {
    Column {
        Text("Cuéntanos sobre ti", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = name, onValueChange = onName,
            label = { Text("Nombre completo") },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = phone, onValueChange = onPhone,
            label = { Text("Teléfono (opcional)") },
            leadingIcon = { Icon(Icons.Default.Phone, null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Busco en Fix it...", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RoleOption(
                title = "Contratar", desc = "Busco profesionales", icon = Icons.Default.Search, 
                selected = role == "CLIENT", onClick = { onRole("CLIENT") }, modifier = Modifier.weight(1f)
            )
            RoleOption(
                title = "Trabajar", desc = "Ofrezco servicios", icon = Icons.Default.Work, 
                selected = role == "PROVIDER", onClick = { onRole("PROVIDER") }, modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun RoleOption(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = if (selected) Color(0xFFFEF9C3) else BackgroundGray),
        border = androidx.compose.foundation.BorderStroke(2.dp, if (selected) YellowDark else OutlineGray)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
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
        Text("Detalles profesionales", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Spacer(modifier = Modifier.height(16.dp))
        
        // Placeholder for category selection
        OutlinedTextField(
            value = "", onValueChange = {},
            label = { Text("Especialidad / Categoría") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("Movilidad y disponibilidad", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Checkbox(checked = hasVehicle, onCheckedChange = onVehicle, colors = CheckboxDefaults.colors(checkedColor = PrimaryYellow))
            Text("Cuento con vehículo propio", fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Checkbox(checked = canTravel, onCheckedChange = onTravel, colors = CheckboxDefaults.colors(checkedColor = PrimaryYellow))
            Text("Puedo viajar a otros municipios", fontSize = 14.sp)
        }
    }
}
