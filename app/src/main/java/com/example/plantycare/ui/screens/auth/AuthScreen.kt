package com.example.plantycare.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.plantycare.R
import com.example.plantycare.ui.theme.*
import com.example.plantycare.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    profileViewModel: ProfileViewModel,
    onAuthSuccess: () -> Unit
) {
    // Mode switcher: false = Login (Masuk), true = Sign Up (Daftar)
    var isSignUp by remember { mutableStateOf(false) }

    // Form inputs
    var displayName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Auto-sliding Carousel Plant Images (High-Res Botanical Photography)
    val carouselImages = remember {
        listOf(
            "https://images.unsplash.com/photo-1463936575829-25148e1db1b8?w=800&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=800&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1545241047-6083a3684587?w=800&auto=format&fit=crop&q=80",
            "https://images.unsplash.com/photo-1512428813834-c702c7702b78?w=800&auto=format&fit=crop&q=80"
        )
    }

    val pagerState = rememberPagerState(pageCount = { carouselImages.size })

    // Auto-slide effect every 3.5 seconds
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3500)
            val nextPage = (pagerState.currentPage + 1) % carouselImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.uiEvent.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        containerColor = CreamBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TOP SECTION: AUTO-SLIDING REAL PLANT PHOTO CAROUSEL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = carouselImages[page],
                        contentDescription = "Header Plant Carousel",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Gradient Overlay to blend smoothly into the Cream Background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    CreamBackground.copy(alpha = 0.5f),
                                    CreamBackground
                                )
                            )
                        )
                )

                // Dot Page Indicators
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(carouselImages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) SagePrimary else Color.White.copy(alpha = 0.6f))
                        )
                    }
                }
            }

            // MAIN LOGIN & SIGNUP CONTENT AREA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Official Brand Logo in rounded squircle
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(Color.White)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(26.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_plantycare),
                        contentDescription = "PlantyCare Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "plantycare",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = SagePrimary,
                    fontSize = 28.sp
                )
                Text(
                    text = if (isSignUp) "Buat akun baru untuk mulai berkebun" else "Masuk untuk memantau tanamanmu",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // FORM FIELDS CARD
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        AnimatedVisibility(visible = isSignUp) {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                OutlinedTextField(
                                    value = displayName,
                                    onValueChange = { displayName = it },
                                    label = { Text("Nama Lengkap") },
                                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SagePrimary) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SagePrimary,
                                        unfocusedBorderColor = BorderSubtle
                                    )
                                )

                                OutlinedTextField(
                                    value = username,
                                    onValueChange = { username = it },
                                    label = { Text("Username") },
                                    prefix = { Text("@") },
                                    leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null, tint = SagePrimary) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SagePrimary,
                                        unfocusedBorderColor = BorderSubtle
                                    )
                                )
                            }
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Alamat Email") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SagePrimary) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SagePrimary,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Kata Sandi") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SagePrimary) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = TextSecondary
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SagePrimary,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                if (isSignUp) {
                                    profileViewModel.register(
                                        displayName = displayName,
                                        username = username,
                                        email = email,
                                        password = password,
                                        onSuccess = onAuthSuccess
                                    )
                                } else {
                                    profileViewModel.login(
                                        email = if (email.isBlank()) "nabil@plantycare.app" else email,
                                        password = if (password.isBlank()) "123456" else password,
                                        onSuccess = onAuthSuccess
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SagePrimary)
                        ) {
                            Text(
                                text = if (isSignUp) "Daftar Akun Baru" else "Masuk ke PlantyCare",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // CLEAN HYPERLINK-STYLE SWITCHER: "Belum punya akun? Daftar Sekarang"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isSignUp) "Sudah punya akun? " else "Belum punya akun? ",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = if (isSignUp) "Masuk" else "Daftar Sekarang",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SagePrimary,
                        modifier = Modifier
                            .clickable { isSignUp = !isSignUp }
                            .padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Guest Mode Shortcut
                TextButton(
                    onClick = {
                        profileViewModel.login("guest@plantycare.app", "123456", onAuthSuccess)
                    }
                ) {
                    Text(
                        text = "Lanjutkan sebagai Tamu (Guest) →",
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
