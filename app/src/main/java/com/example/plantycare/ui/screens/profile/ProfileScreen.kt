package com.example.plantycare.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.plantycare.ui.theme.*
import com.example.plantycare.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val profileState by profileViewModel.profile.collectAsState()
    val plantCount by profileViewModel.plantCount.collectAsState()
    val wateringCount by profileViewModel.wateringCount.collectAsState()

    var isEditMode by remember { mutableStateOf(false) }

    var displayName by remember(profileState) { mutableStateOf(profileState?.displayName ?: "") }
    var username by remember(profileState) { mutableStateOf(profileState?.username ?: "") }
    var email by remember(profileState) { mutableStateOf(profileState?.email ?: "") }
    var selectedImageUri by remember(profileState) { mutableStateOf<Uri?>(profileState?.photoUri?.let { Uri.parse(it) }) }

    val snackbarHostState = remember { SnackbarHostState() }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            // Save photo immediately
            profileViewModel.updateProfile(
                displayName = displayName,
                username = username,
                email = email,
                photoUri = it.toString()
            )
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.uiEvent.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        containerColor = CreamBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profil Pengguna",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // PHOTO PROFILE AVATAR WITH CAMERA BADGE
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clickable { photoPickerLauncher.launch("image/*") }
            ) {
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .clip(CircleShape)
                        .background(SageLight)
                        .border(3.dp, SagePrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Foto Profil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(text = "🧑‍🌾", fontSize = 52.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(SagePrimary)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Ubah Foto",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = profileState?.displayName ?: "Pengguna PlantyCare",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "@${profileState?.username ?: "gardener"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            // STATS COUNTERS (ROOM SQLITE DATA)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$plantCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text(text = "Tanaman", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Divider(modifier = Modifier.height(34.dp).width(1.dp), color = BorderSubtle)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$wateringCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = WaterAlert)
                        Text(text = "Disiram", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Divider(modifier = Modifier.height(34.dp).width(1.dp), color = BorderSubtle)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Level 3", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = SagePrimary)
                        Text(text = "Green Thumb", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
            }

            // INFORMASI AKUN CARD (READ-ONLY BY DEFAULT, EDITABLE VIA PENCIL ICON)
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(26.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Header with Edit Pencil Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Informasi Akun",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (isEditMode) {
                                Surface(color = SageLight, shape = RoundedCornerShape(8.dp)) {
                                    Text(
                                        text = "Mode Edit",
                                        color = SagePrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // THE PENCIL ICON BUTTON
                        IconButton(
                            onClick = {
                                isEditMode = !isEditMode
                                if (!isEditMode) {
                                    // Reset on cancel
                                    displayName = profileState?.displayName ?: ""
                                    username = profileState?.username ?: ""
                                    email = profileState?.email ?: ""
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isEditMode) SageContainer else SurfaceSubtle)
                        ) {
                            Icon(
                                imageVector = if (isEditMode) Icons.Default.Close else Icons.Default.Edit,
                                contentDescription = "Edit Akun",
                                tint = SagePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isEditMode) {
                        // VIEW MODE (Read-only aesthetic list)
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            InfoRow(label = "Nama Tampilan", value = profileState?.displayName ?: "-")
                            Divider(color = BorderSubtle, thickness = 0.5.dp)
                            InfoRow(label = "Username", value = "@${profileState?.username ?: "-"}")
                            Divider(color = BorderSubtle, thickness = 0.5.dp)
                            InfoRow(label = "Alamat Email", value = profileState?.email ?: "-")
                            Divider(color = BorderSubtle, thickness = 0.5.dp)
                            InfoRow(label = "Status Akun", value = "Aktif & Terverifikasi ✓")
                        }
                    } else {
                        // EDIT MODE (Editable form fields)
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = displayName,
                                onValueChange = { displayName = it },
                                label = { Text("Nama Tampilan") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SagePrimary) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SagePrimary, unfocusedBorderColor = BorderSubtle)
                            )

                            OutlinedTextField(
                                value = username,
                                onValueChange = { username = it },
                                label = { Text("Username") },
                                prefix = { Text("@") },
                                leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, tint = SagePrimary) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SagePrimary, unfocusedBorderColor = BorderSubtle)
                            )

                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SagePrimary) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SagePrimary, unfocusedBorderColor = BorderSubtle)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        isEditMode = false
                                        displayName = profileState?.displayName ?: ""
                                        username = profileState?.username ?: ""
                                        email = profileState?.email ?: ""
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text("Batal", color = TextSecondary)
                                }

                                Button(
                                    onClick = {
                                        profileViewModel.updateProfile(
                                            displayName = displayName,
                                            username = username,
                                            email = email,
                                            photoUri = selectedImageUri?.toString()
                                        )
                                        isEditMode = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SagePrimary)
                                ) {
                                    Text("Simpan", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // LOGOUT BUTTON
            OutlinedButton(
                onClick = {
                    profileViewModel.logout {
                        onLogoutSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD16C23))
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFD16C23), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Keluar dari Akun (Logout)", color = Color(0xFFD16C23), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}
