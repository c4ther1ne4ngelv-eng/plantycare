package com.example.plantycare.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
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
import com.example.plantycare.ui.viewmodel.PlantViewModel

// Plant Catalog Model with Accurate Real High-Res Photos
data class PlantCatalogItem(
    val name: String,
    val species: String,
    val category: String,
    val photoUrl: String,
    val description: String,
    val minLux: Float,
    val maxLux: Float,
    val defaultIntervalDays: Int,
    val defaultIntervalHours: Int = 0,  // 0 = pakai days; >0 = tanaman yang perlu air beberapa jam sekali
    val lightDescription: String,
    val waterDescription: String
)

val PLANT_CATALOG = listOf(
    PlantCatalogItem(
        name = "Kaktus Koboi",
        species = "Cereus peruvianus",
        category = "Sukulen & Kaktus",
        photoUrl = "https://images.unsplash.com/photo-1509587584298-0f3b3a3a1797?w=600&auto=format&fit=crop&q=80",
        description = "Tanaman kaktus gurun kokoh yang sangat tahan kekeringan. Tumbuh tegak dan sangat cocok diletakkan di dekat jendela yang banyak terkena sinar matahari.",
        minLux = 2500f,
        maxLux = 6000f,
        defaultIntervalDays = 14,
        lightDescription = "Bright Direct (2.500–6.000 Lux)",
        waterDescription = "Siram setiap 14 hari sekali saat tanah benar-benar kering."
    ),
    PlantCatalogItem(
        name = "Lidah Buaya",
        species = "Aloe barbadensis miller",
        category = "Sukulen & Kaktus",
        photoUrl = "https://images.unsplash.com/photo-1567689265664-1c48de61db0b?w=600&auto=format&fit=crop&q=80",
        description = "Tanaman sukulen herbal kaya gel dingin serbaguna. Daun tebal berduri halus, sangat mudah dirawat di dalam pot meja.",
        minLux = 1500f,
        maxLux = 4000f,
        defaultIntervalDays = 10,
        lightDescription = "Bright Indirect (1.500–4.000 Lux)",
        waterDescription = "Siram setiap 10 hari sekali. Jangan biarkan air menggenang di sela daun."
    ),
    PlantCatalogItem(
        name = "Bunga Lili Perdamaian",
        species = "Spathiphyllum",
        category = "Bunga Indah",
        photoUrl = "https://images.unsplash.com/photo-1620127814424-63c8120e5881?w=600&auto=format&fit=crop&q=80",
        description = "Bunga putih anggun penyerap polutan udara nomor satu dari NASA. Daun hijau mengilap akan sedikit menunduk saat butuh disiram.",
        minLux = 500f,
        maxLux = 1500f,
        defaultIntervalDays = 3,
        lightDescription = "Low to Medium (500–1.500 Lux)",
        waterDescription = "Siram setiap 3 hari sekali untuk menjaga media tanam tetap lembap."
    ),
    PlantCatalogItem(
        name = "Bunga Mawar Mini",
        species = "Rosa chinensis minima",
        category = "Bunga Indah",
        photoUrl = "https://images.unsplash.com/photo-1559563458-527698bf5295?w=600&auto=format&fit=crop&q=80",
        description = "Mawar kerdil beraroma manis yang mekar indah. Membutuhkan sirkulasi udara baik dan asupan sinar matahari pagi yang cukup agar kuncup rajin bermekaran.",
        minLux = 3000f,
        maxLux = 6000f,
        defaultIntervalDays = 2,
        lightDescription = "Full Sunlight (>3.000 Lux)",
        waterDescription = "Siram setiap 2 hari sekali pada pangkal tanah, hindari menyiram kelopak."
    ),
    PlantCatalogItem(
        name = "Bunga Tulip",
        species = "Tulipa gesneriana",
        category = "Bunga Indah",
        photoUrl = "https://images.unsplash.com/photo-1520763185298-1b434c919102?w=600&auto=format&fit=crop&q=80",
        description = "Bunga legendaris musim semi dengan kelopak piala yang menawan. Menyukai suhu yang sejuk dan media tanam yang berdrainase baik.",
        minLux = 1800f,
        maxLux = 3500f,
        defaultIntervalDays = 4,
        lightDescription = "Moderate Sun (1.800–3.500 Lux)",
        waterDescription = "Siram setiap 4 hari sekali saat lapisan atas media tanam mulai mengering."
    ),
    PlantCatalogItem(
        name = "Monstera Deliciosa",
        species = "Monstera deliciosa",
        category = "Daun Hias",
        photoUrl = "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=600&auto=format&fit=crop&q=80",
        description = "Ikon tanaman hias tropis dengan daun berlubang artistik. Menyukai tempat terang namun terhindar dari terik matahari siang langsung.",
        minLux = 1000f,
        maxLux = 2500f,
        defaultIntervalDays = 4,
        lightDescription = "Medium Dappled (1.000–2.500 Lux)",
        waterDescription = "Siram setiap 4 hari sekali saat 2 cm tanah teratas mengering."
    ),
    PlantCatalogItem(
        name = "Lidah Mertua",
        species = "Sansevieria trifasciata",
        category = "Daun Hias",
        photoUrl = "https://images.unsplash.com/photo-1593482892290-f54927ae1bf6?w=600&auto=format&fit=crop&q=80",
        description = "Tanaman pembersih racun udara paling tangguh. Tahan banting, memproduksi oksigen malam hari, dan sangat adaptif di ruangan ber-AC.",
        minLux = 400f,
        maxLux = 2000f,
        defaultIntervalDays = 7,
        lightDescription = "Toleransi Tinggi (400–2.000 Lux)",
        waterDescription = "Siram setiap 7 hari sekali. Sangat tahan terhadap kekeringan."
    ),
    PlantCatalogItem(
        name = "Sirih Gading",
        species = "Epipremnum aureum",
        category = "Daun Hias",
        photoUrl = "https://images.unsplash.com/photo-1617173944883-6ffbd35d584d?w=600&auto=format&fit=crop&q=80",
        description = "Tanaman rambat bercorak daun emas yang tumbuh sangat cepat. Sangat cocok ditaruh di rak buku, meja kerja, atau pot gantung.",
        minLux = 600f,
        maxLux = 1800f,
        defaultIntervalDays = 5,
        lightDescription = "Low to Medium (600–1.800 Lux)",
        waterDescription = "Siram setiap 5 hari sekali ketika media tanam mulai kering."
    ),
    PlantCatalogItem(
        name = "Anggrek Bulan",
        species = "Phalaenopsis amabilis",
        category = "Bunga Indah",
        photoUrl = "https://images.unsplash.com/photo-1525310072745-f49212b5ac6d?w=600&auto=format&fit=crop&q=80",
        description = "Puspa Pesona Nusantara dengan mahkota bunga anggun yang mekar berbulan-bulan. Butuh sirkulasi udara akar yang baik dan cahaya baur lembut.",
        minLux = 1200f,
        maxLux = 2200f,
        defaultIntervalDays = 6,
        lightDescription = "Filtered Light (1.200–2.200 Lux)",
        waterDescription = "Siram setiap 6 hari sekali, gunakan semprotan halus pada media arang/pakis."
    ),

    // === TANAMAN INTENSIF: PERLU AIR SETIAP BEBERAPA JAM ===
    PlantCatalogItem(
        name = "Selada Air",
        species = "Nasturtium officinale",
        category = "Tanaman Intensif",
        photoUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=600&auto=format&fit=crop&q=80",
        description = "Sayuran akuatik super segar yang tumbuh di air atau media sangat basah. Akar harus selalu terendam atau lembap ekstrem — wajib disiram setiap 6 jam agar tidak layu.",
        minLux = 1500f,
        maxLux = 3000f,
        defaultIntervalDays = 0,
        defaultIntervalHours = 6,
        lightDescription = "Medium to Bright (1.500–3.000 Lux)",
        waterDescription = "Siram setiap 6 JAM sekali! Tanaman akuatik ini tidak boleh kekeringan."
    ),
    PlantCatalogItem(
        name = "Bayam Merah Mini",
        species = "Amaranthus tricolor",
        category = "Tanaman Intensif",
        photoUrl = "https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=600&auto=format&fit=crop&q=80",
        description = "Sayuran daun merah menyala kaya antioksidan yang tumbuh cepat. Bibit muda sangat sensitif kekeringan — tanah harus selalu lembap di cuaca panas.",
        minLux = 2000f,
        maxLux = 5000f,
        defaultIntervalDays = 0,
        defaultIntervalHours = 8,
        lightDescription = "Full to Partial Sun (2.000–5.000 Lux)",
        waterDescription = "Siram setiap 8 JAM sekali di musim kemarau agar bibit tidak gosong."
    ),
    PlantCatalogItem(
        name = "Rumput Hias Biru",
        species = "Festuca glauca",
        category = "Tanaman Intensif",
        photoUrl = "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=600&auto=format&fit=crop&q=80",
        description = "Rumput ornamental berkilau biru keperakan dengan tekstur halus seperti rambut. Sangat populer untuk taman pot minimalis, butuh kelembapan rutin agar warna tetap cerah.",
        minLux = 2500f,
        maxLux = 6000f,
        defaultIntervalDays = 0,
        defaultIntervalHours = 12,
        lightDescription = "Full Sun (2.500–6.000 Lux)",
        waterDescription = "Siram setiap 12 JAM sekali di musim kering agar tetap segar dan biru."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlantScreen(
    plantViewModel: PlantViewModel,
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var selectedCatalogItem by remember { mutableStateOf<PlantCatalogItem?>(null) }

    val liveLux by plantViewModel.liveLux.collectAsState()

    val categories = listOf("Semua", "Sukulen & Kaktus", "Bunga Indah", "Daun Hias", "Tanaman Intensif")

    val filteredList = remember(searchQuery, selectedCategory) {
        PLANT_CATALOG.filter { item ->
            val matchesCategory = selectedCategory == "Semua" || item.category == selectedCategory
            val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) ||
                    item.species.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        containerColor = CreamBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pilih Jenis Tanaman",
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // SEARCH BAR
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari kaktus, mawar, lili, tulip, dll...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SagePrimary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SagePrimary,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // CATEGORY FILTER CHIPS
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        color = if (isSelected) SagePrimary else Color.White,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .border(1.dp, if (isSelected) SagePrimary else BorderSubtle, RoundedCornerShape(14.dp))
                            .clickable { selectedCategory = category }
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // CATALOG LIST WITH REAL HIGH-RES PHOTOS
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList, key = { it.name }) { item ->
                    CatalogPlantCard(
                        item = item,
                        onClick = { selectedCatalogItem = item }
                    )
                }
            }
        }

        // PLANT DETAIL MODAL SHEET / DIALOG WITH REAL PHOTO
        selectedCatalogItem?.let { catalogItem ->
            PlantDetailModalDialog(
                item = catalogItem,
                currentLux = liveLux,
                onDismiss = { selectedCatalogItem = null },
                onConfirmAdd = { customName, location, interval ->
                    plantViewModel.addPlant(
                        name = customName,
                        location = location,
                        minLux = catalogItem.minLux,
                        maxLux = catalogItem.maxLux,
                        intervalDays = interval,
                        intervalHours = catalogItem.defaultIntervalHours,
                        photoUri = catalogItem.photoUrl,
                        onSuccess = {
                            selectedCatalogItem = null
                            onNavigateBack()
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun CatalogPlantCard(
    item: PlantCatalogItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderSubtle, RoundedCornerShape(22.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ACCURATE REAL PHOTO WITH SUBTLE PLACEHOLDER BACKGROUND
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFE4EDE2))
            ) {
                AsyncImage(
                    model = item.photoUrl,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = item.species,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "☀️ ${item.lightDescription.split('(').first().trim()}",
                        fontSize = 10.sp,
                        color = SagePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (item.defaultIntervalHours > 0)
                            "💧 ${item.defaultIntervalHours}j sekali"
                        else
                            "💧 ${item.defaultIntervalDays}h sekali",
                        fontSize = 10.sp,
                        color = WaterBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Surface(
                color = SageLight,
                shape = CircleShape,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "+", color = SagePrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun PlantDetailModalDialog(
    item: PlantCatalogItem,
    currentLux: Float,
    onDismiss: () -> Unit,
    onConfirmAdd: (customName: String, location: String, interval: Int) -> Unit
) {
    var customName by remember { mutableStateOf(item.name) }
    var location by remember { mutableStateOf("Ruang Tamu") }
    var intervalDays by remember { mutableStateOf(item.defaultIntervalDays) }

    val isCurrentRoomOptimal = currentLux in item.minLux..item.maxLux

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE4EDE2))
                    ) {
                        AsyncImage(
                            model = item.photoUrl,
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column {
                        Text(text = item.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Text(text = item.species, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Description Card
                Surface(
                    color = SurfaceSubtle,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        modifier = Modifier.padding(12.dp),
                        lineHeight = 18.sp
                    )
                }

                // LIGHT DETAIL (WITH REAL-TIME SENSOR COMPARISON)
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SageLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = SagePrimary, modifier = Modifier.size(16.dp))
                            Text(text = "Kebutuhan Cahaya", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SagePrimary)
                        }
                        Text(text = item.lightDescription, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isCurrentRoomOptimal)
                                "✓ Ruangan ini (%.0f Lux) SANGAT COCOK!".format(currentLux)
                            else if (currentLux < item.minLux)
                                "⚠️ Ruangan ini (%.0f Lux) terlalu redup untuk tanaman ini.".format(currentLux)
                            else
                                "⚠️ Ruangan ini (%.0f Lux) terlalu terik.".format(currentLux),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrentRoomOptimal) SagePrimary else Color(0xFFC7483B)
                        )
                    }
                }

                // WATER DETAIL
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = WaterBlueLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.WaterDrop, contentDescription = null, tint = WaterBlue, modifier = Modifier.size(16.dp))
                            Text(text = "Kebutuhan Air", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = WaterBlue)
                        }
                        Text(text = item.waterDescription, fontSize = 11.sp, color = TextPrimary)
                    }
                }

                // LOCATION INPUT
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Lokasi Penempatan Pot") },
                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = SagePrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SagePrimary, unfocusedBorderColor = BorderSubtle)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmAdd(customName, location, intervalDays) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SagePrimary)
            ) {
                Text(text = "Tambahkan ke Kebun Saya 🌿", fontWeight = FontWeight.Bold)
            }
        }
    )
}
