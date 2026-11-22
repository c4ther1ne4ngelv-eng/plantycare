package com.example.plantycare.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.plantycare.R
import com.example.plantycare.data.local.entity.PlantEntity
import com.example.plantycare.data.local.entity.UserProfileEntity
import com.example.plantycare.ui.theme.*
import com.example.plantycare.ui.viewmodel.PlantViewModel
import com.example.plantycare.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    plantViewModel: PlantViewModel,
    profileViewModel: ProfileViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val plants by plantViewModel.plants.collectAsState()
    val liveLux by plantViewModel.liveLux.collectAsState()
    val userProfile by profileViewModel.profile.collectAsState()

    // Check if any plant needs watering today
    val plantsNeedingWater = plants.filter { plant ->
    if (plant.wateringIntervalHours > 0) {
        val hoursSince = (System.currentTimeMillis() - plant.lastWateredTimestamp) / (60 * 60 * 1000L)
        hoursSince >= plant.wateringIntervalHours
    } else {
        val daysSince = (System.currentTimeMillis() - plant.lastWateredTimestamp) / (24 * 60 * 60 * 1000L)
        daysSince >= plant.wateringIntervalDays
    }
}

    Scaffold(
        containerColor = CreamBackground,
        topBar = {
            HomeTopBar(
                userProfile = userProfile,
                onProfileClick = onNavigateToProfile
            )
        },
        bottomBar = {
            FloatingBottomNav(
                currentScreen = "home",
                onHomeClick = {},
                onHistoryClick = onNavigateToHistory,
                onProfileClick = onNavigateToProfile,
                onAddClick = onNavigateToAdd
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HERO CARD: Live Ambient Light Sensor
            item {
                LiveLightHeroCard(luxValue = liveLux)
            }

            // SMART VIBRATION ALERT: Plants Needing Water
            if (plantsNeedingWater.isNotEmpty()) {
                item {
                    NeedsWaterAlertCard(
                        count = plantsNeedingWater.size,
                        firstPlantName = plantsNeedingWater.first().name,
                        onClick = { onNavigateToDetail(plantsNeedingWater.first().id) },
                        onVibrateAgain = { plantViewModel.triggerWateringAlert() }
                    )
                }
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Koleksi Tanaman (${plants.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "+ Tambah",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = SagePrimary,
                        modifier = Modifier.clickable { onNavigateToAdd() }
                    )
                }
            }

            // Plant Items with Real Photos
            if (plants.isEmpty()) {
                item {
                    EmptyPlantCard(onAddClick = onNavigateToAdd)
                }
            } else {
                items(plants, key = { it.id }) { plant ->
                    PlantItemCard(
                        plant = plant,
                        currentLux = liveLux,
                        onClick = { onNavigateToDetail(plant.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTopBar(
    userProfile: UserProfileEntity?,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_plantycare),
                    contentDescription = "PlantyCare Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column {
                Text(
                    text = "plantycare",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = SagePrimary
                )
                Text(
                    text = "Halo, ${userProfile?.displayName ?: "Pengguna"}! 🌱",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // TOP RIGHT: User Profile Avatar (Navigates to Profile Page)
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(SageLight)
                .border(2.dp, SagePrimary, CircleShape)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            if (!userProfile?.photoUri.isNullOrEmpty()) {
                AsyncImage(
                    model = userProfile?.photoUri,
                    contentDescription = "Foto Profil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(text = "🧑‍🌾", fontSize = 22.sp)
            }
        }
    }
}

@Composable
fun LiveLightHeroCard(luxValue: Float) {
    val isOptimal = luxValue in 800f..3000f
    val statusText = if (isOptimal) "Optimal untuk Tanaman Indoor" else if (luxValue < 800f) "Cahaya Redup" else "Sinar Terik"

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF4A6644), Color(0xFF2E4529))
                ),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "☀️ SENSOR CAHAYA RUANGAN",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD6E8D2),
                    letterSpacing = 1.sp
                )
                Surface(
                    color = Color(0xFF6B8A64),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "LIVE",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "%.0f".format(luxValue),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lux",
                            fontSize = 16.sp,
                            color = Color(0xFFD8E7D4),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "✓ $statusText",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✨", fontSize = 24.sp)
                }
            }
        }
    }
}

@Composable
fun NeedsWaterAlertCard(
    count: Int,
    firstPlantName: String,
    onClick: () -> Unit,
    onVibrateAgain: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = WaterAlertBg),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, WaterAlert.copy(alpha = 0.3f), RoundedCornerShape(22.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(WaterAlert.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = WaterAlert,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "$count Tanaman Butuh Air!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WaterAlert
                        )
                        Text(
                            text = "$firstPlantName sudah jadwalnya disiram",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 1.dp
                ) {
                    Text(
                        text = "Siram",
                        color = WaterAlert,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "📳 Pengingat Getar Otomatis Aktif",
                    fontSize = 10.sp,
                    color = WaterAlert,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Uji Getar",
                    fontSize = 10.sp,
                    color = SagePrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onVibrateAgain() }
                )
            }
        }
    }
}

@Composable
fun PlantItemCard(
    plant: PlantEntity,
    currentLux: Float,
    onClick: () -> Unit
) {
    val daysSinceWatered = ((System.currentTimeMillis() - plant.lastWateredTimestamp) / (24 * 60 * 60 * 1000L)).toInt()
    val hoursSinceWatered = ((System.currentTimeMillis() - plant.lastWateredTimestamp) / (60 * 60 * 1000L)).toInt()
    val intervalDays = plant.wateringIntervalDays
    val intervalHours = plant.wateringIntervalHours
    val isDue = if (intervalHours > 0) {
        hoursSinceWatered >= intervalHours
    } else {
        daysSinceWatered >= intervalDays
    }
    val left = if (intervalHours > 0) {
        intervalHours - hoursSinceWatered
    } else {
        intervalDays - daysSinceWatered
    }
    val isLightAdequate = currentLux in plant.minLux..plant.maxLux

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // LEFT CONTENT (PHOTO + TEXT) WITH WEIGHT SO IT NEVER OVERFLOWS OR SQUISHES THE BADGE
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFE4EDE2))
                ) {
                    AsyncImage(
                        model = if (plant.photoUri.isNotBlank()) plant.photoUri else "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=600&auto=format&fit=crop&q=80",
                        contentDescription = plant.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = plant.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${plant.location} • %.0f-%.0f Lux".format(plant.minLux, plant.maxLux),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = if (isLightAdequate) SageLight else Color(0xFFFBEBE8),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isLightAdequate) "● Cukup Cahaya" else "● Cahaya Kurang Pas",
                            color = if (isLightAdequate) SagePrimary else Color(0xFFC7483B),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // RIGHT WATERING COUNTDOWN BADGE (FIXED SHAPE - NEVER SQUASHED OR CUT OFF)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isDue) WaterAlertBg else Color(0xFFF2F7F0),
                border = BorderStroke(1.5.dp, if (isDue) WaterAlert else SageSecondary.copy(alpha = 0.5f)),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .widthIn(min = 60.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isDue) "Siram" else "${left}h",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isDue) WaterAlert else SagePrimary,
                        maxLines = 1
                    )
                    Text(
                        text = if (isDue) "Hari ini!" else "lagi",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDue) WaterAlert else TextSecondary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyPlantCard(onAddClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SageLight)
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=600&auto=format&fit=crop&q=80",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Belum Ada Tanaman",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Pilih tanaman dari katalog lengkap kami!",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "+ Buka Katalog Tanaman", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FloatingBottomNav(
    currentScreen: String,
    onHomeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp, start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF283A24),
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onHomeClick) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🌱", fontSize = 16.sp)
                        Text(text = "Home", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(SageSecondary)
                        .clickable { onAddClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah Tanaman",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = onHistoryClick) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFFB5CAB1), modifier = Modifier.size(20.dp))
                        Text(text = "Riwayat", fontSize = 9.sp, color = Color(0xFFB5CAB1))
                    }
                }

                IconButton(onClick = onProfileClick) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFB5CAB1), modifier = Modifier.size(20.dp))
                        Text(text = "Profil", fontSize = 9.sp, color = Color(0xFFB5CAB1))
                    }
                }
            }
        }
    }
}
