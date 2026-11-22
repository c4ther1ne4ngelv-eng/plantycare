package com.example.plantycare.ui.screens.detail

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.plantycare.ui.theme.*
import com.example.plantycare.ui.viewmodel.PlantViewModel
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(
    plantId: Long,
    plantViewModel: PlantViewModel,
    onNavigateBack: () -> Unit
) {
    // STABLE FLOW CACHED WITH remember(plantId) -> PREVENTS RECOMPOSITION FLICKERING/STUTTERING
    val plantState by remember(plantId) {
        plantViewModel.getPlant(plantId)
    }.collectAsState(initial = null)

    val liveLux by plantViewModel.liveLux.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Photo picker bottom sheet state
    var showPhotoBottomSheet by remember { mutableStateOf(false) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    // --- GALLERY PICKER ---
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            plantViewModel.updatePlantPhoto(plantId, it.toString())
        }
    }

    // --- CAMERA CAPTURE ---
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            cameraImageUri?.let { uri ->
                plantViewModel.updatePlantPhoto(plantId, uri.toString())
            }
        }
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createCameraUri(context)
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        }
    }

    fun launchCamera() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            val uri = createCameraUri(context)
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Motion Shake Sensor listener
    LaunchedEffect(plantState?.id) {
        plantState?.let { plant ->
            plantViewModel.shakeEvent.collectLatest {
                plantViewModel.waterPlant(plant, isShake = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        plantViewModel.snackbarMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Photo source bottom sheet
    if (showPhotoBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPhotoBottomSheet = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Ganti Foto Tanaman",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Pilih sumber foto yang ingin kamu gunakan.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Camera option
                Surface(
                    color = SageLight,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showPhotoBottomSheet = false
                            launchCamera()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SagePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text("Ambil Foto Baru", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                            Text("Gunakan kamera untuk foto langsung", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }

                // Gallery option
                Surface(
                    color = Color(0xFFE8F4FD),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showPhotoBottomSheet = false
                            galleryLauncher.launch("image/*")
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(WaterBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text("Pilih dari Galeri", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                            Text("Pilih foto dari galeri ponselmu", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = CreamBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = plantState?.name ?: "Detail Tanaman",
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
                actions = {
                    plantState?.let { plant ->
                        IconButton(onClick = { plantViewModel.deletePlant(plant, onNavigateBack) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFFC7483B))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        }
    ) { innerPadding ->
        val plant = plantState
        if (plant == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SagePrimary)
            }
        } else {
            val isLightAdequate = liveLux in plant.minLux..plant.maxLux
            val statusLight = if (isLightAdequate) "Optimal" else if (liveLux < plant.minLux) "Terlalu Redup" else "Terlalu Terik"

            // Hourly vs Daily watering logic
            val isDue: Boolean
            val wateringLabel: String
            val timeLeftLabel: String

            if (plant.wateringIntervalHours > 0) {
                val hoursSinceWatered = ((System.currentTimeMillis() - plant.lastWateredTimestamp) / (60 * 60 * 1000L)).toInt()
                val hoursLeft = plant.wateringIntervalHours - hoursSinceWatered
                isDue = hoursLeft <= 0
                wateringLabel = "⏱️ Interval: Setiap ${plant.wateringIntervalHours} Jam"
                timeLeftLabel = if (isDue) "Sudah waktunya!" else "$hoursLeft jam lagi"
            } else {
                val daysSinceWatered = ((System.currentTimeMillis() - plant.lastWateredTimestamp) / (24 * 60 * 60 * 1000L)).toInt()
                val daysLeft = plant.wateringIntervalDays - daysSinceWatered
                isDue = daysLeft <= 0
                wateringLabel = "💧 Interval: ${plant.wateringIntervalDays} Hari Sekali"
                timeLeftLabel = if (isDue) "Sudah waktunya!" else "$daysLeft hari lagi"
            }

            val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }
            val lastWateredStr = dateFormat.format(Date(plant.lastWateredTimestamp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // HERO BANNER WITH REAL PHOTO + EDIT BUTTON
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderSubtle, RoundedCornerShape(28.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Clickable plant photo box — tap to change photo
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(Color(0xFFE4EDE2))
                                .clickable { showPhotoBottomSheet = true },
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            AsyncImage(
                                model = if (plant.photoUri.isNotBlank()) plant.photoUri
                                        else "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=600&auto=format&fit=crop&q=80",
                                contentDescription = plant.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Edit badge
                            Box(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(SagePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Ganti Foto",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ketuk foto untuk menggantinya",
                            fontSize = 10.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = plant.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text(text = "Lokasi: ${plant.location}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = SageLight, shape = RoundedCornerShape(10.dp)) {
                                Text(
                                    text = wateringLabel,
                                    color = SagePrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            if (isDue) {
                                Surface(color = WaterAlertBg, shape = RoundedCornerShape(10.dp)) {
                                    Text(
                                        text = "📳 Butuh Air!",
                                        color = WaterAlert,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                Surface(color = Color(0xFFE8F4FD), shape = RoundedCornerShape(10.dp)) {
                                    Text(
                                        text = "⏳ $timeLeftLabel",
                                        color = WaterBlue,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // SMART WATERING VIBRATION ALERT CARD (IF DUE)
                AnimatedVisibility(visible = isDue) {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = WaterAlertBg),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, WaterAlert.copy(alpha = 0.3f), RoundedCornerShape(22.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = WaterAlert)
                                Column {
                                    Text(
                                        text = "Waktunya Menyiram!",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = WaterAlert
                                    )
                                    Text(
                                        text = "Sensor getar otomatis memicu getaran ponsel untuk mengingatkanmu.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            OutlinedButton(
                                onClick = { plantViewModel.triggerWateringAlert() },
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, WaterAlert)
                            ) {
                                Text("Uji Getar", color = WaterAlert, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // SENSOR 1: ENVIRONMENT SENSOR (LIGHT METER GAUGE)
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderSubtle, RoundedCornerShape(26.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "☀️ SENSOR CAHAYA (ENVIRONMENT)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SagePrimary,
                                letterSpacing = 1.sp
                            )
                            Surface(color = SageLight, shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "SENSOR_LIGHT",
                                    color = SagePrimary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Custom Circular Gauge Canvas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Box(
                                modifier = Modifier.size(110.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val strokeWidth = 10.dp.toPx()
                                    drawArc(
                                        color = Color(0xFFE4EDE2),
                                        startAngle = 135f,
                                        sweepAngle = 270f,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                    )
                                    val progressRatio = (liveLux / 3500f).coerceIn(0f, 1f)
                                    drawArc(
                                        color = if (isLightAdequate) SagePrimary else Color(0xFFE08836),
                                        startAngle = 135f,
                                        sweepAngle = 270f * progressRatio,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "%.0f".format(liveLux),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                    Text(text = "LUX", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                }
                            }

                            Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                                Text(
                                    text = "Status: $statusLight",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isLightAdequate) SagePrimary else Color(0xFFD16C23)
                                )
                                Text(
                                    text = "Kebutuhan: %.0f - %.0f Lux".format(plant.minLux, plant.maxLux),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = {
                                        plantViewModel.saveLightLog(plant.id, liveLux, statusLight)
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Simpan ke SQLite", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SagePrimary)
                                }
                            }
                        }
                    }
                }

                // SENSOR 2: MOTION SENSOR (ACCELEROMETER SHAKE-TO-WATER)
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = WaterBlueLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, WaterBlue.copy(alpha = 0.25f), RoundedCornerShape(26.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📳 GESTURE SIRAM (MOTION SENSOR)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = WaterBlue,
                                letterSpacing = 1.sp
                            )
                            Surface(color = Color.White, shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "ACCELEROMETER",
                                    color = WaterBlue,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🫗✨", fontSize = 26.sp)
                            }
                            Column {
                                Text(
                                    text = "Goyangkan HP untuk Siram!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E4B66)
                                )
                                Text(
                                    text = "Goyangkan ponselmu dekat pot tanaman untuk mencatat log penyiraman ke SQLite otomatis.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF3F6982)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { plantViewModel.waterPlant(plant, isShake = false) },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WaterBlue)
                        ) {
                            Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Siram Manual Sekarang", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // LAST WATERED INFO
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = "Terakhir disiram: $lastWateredStr",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(14.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/** Creates a temp file URI for camera capture via FileProvider */
private fun createCameraUri(context: Context): Uri {
    val imageFile = File.createTempFile(
        "plant_photo_${System.currentTimeMillis()}",
        ".jpg",
        context.externalCacheDir ?: context.cacheDir
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}
