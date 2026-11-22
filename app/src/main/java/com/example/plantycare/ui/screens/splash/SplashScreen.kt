package com.example.plantycare.ui.screens.splash

import android.graphics.Matrix
import android.graphics.PathMeasure
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.plantycare.R
import com.example.plantycare.ui.theme.BorderSubtle
import com.example.plantycare.ui.theme.CreamBackground
import com.example.plantycare.ui.theme.SagePrimary
import com.example.plantycare.ui.theme.TextSecondary
import com.example.plantycare.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay

/**
 * Apple iOS 15 Multi-Language Handwriting Splash Screen.
 *
 * Uses official Apple SF-Hello vector Bézier paths (from JaceThings/SF-Hello, exported from
 * Apple's official Figma) to render authentic calligraphy strokes via PathMeasure.
 *
 * Greetings cycle:
 *  1. "hello"         (English)       — hello-en
 *  2. "bonjour"       (French)        — hello-fr
 *  3. "halo"          (Indonesian)    — hello-id
 *  4. "selamat datang" (Malay)        — hello-ms

 *
 * Each greeting draws its strokes sequentially with PathMeasure, holds, fades out,
 * then the next language appears. After all greetings the PlantyCare logo fades in.
 */
@Composable
fun SplashScreen(
    profileViewModel: ProfileViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToAuth: () -> Unit
) {
    val userProfile by profileViewModel.profile.collectAsState()

    // ─── OFFICIAL APPLE SF-HELLO VECTOR DATA ────────────────────────────────
    // All paths are in Apple's SVG coordinate space (Y-axis flipped, various viewBoxes).
    // Each entry: list of path strings + viewBox height for Y-flip correction.

    data class HelloLanguage(
        val label: String,
        val paths: List<String>,
        val viewBoxHeight: Float,
        val viewBoxMinX: Float = 0f,
        val viewBoxWidth: Float,
        val flipY: Boolean = true
    )

    val greetings = remember {
        listOf(
            // 1. hello (English) — viewBox="-109 -9 2504 746"
            HelloLanguage(
                label = "hello",
                paths = listOf(
                    "M-109.06069946289062,95.92639923095703 C1.9544999599456787,157.6403045654297 103.11389923095703,236.9969940185547 217.881103515625,372.07550048828125 C296,464.2846984863281 337.9999084472656,569.5725708007812 340,642.1939697265625 C341,696.1920166015625 314.6702880859375,737.156005859375 266,737.156005859375 C212,737.156005859375 178,696.1920166015625 157,602.1610107421875 C134,498.82000732421875 117,380.239990234375 74,0",
                    "M78.21453094482422,37.160953521728516 C100.22924041748047,230.68260192871094 184,372 291,372 C355,372 395.6745910644531,321 384.1253967285156,248 C377.6238098144531,205 370.0873107910156,161 361.3063049316406,110 C351.0714111328125,46 380.3254089355469,-4 468.96173095703125,-4 C598.2246704101562,-4 739.2435302734375,67.83381652832031 811.4124145507812,179.0941619873047 C836,217 846,251 847,284 C848,344 814,389 754,389 C678,389 620,303 620,193 C620,75 684,-8 819.9180908203125,-8 C1004.7244873046875,-8 1209.4246826171875,213.84754943847656 1303.4808349609375,461.42327880859375 C1330.037353515625,531.3258056640625 1340,596.2349243164062 1340,641.593994140625 C1340,695.3764038085938 1323,736.673583984375 1275,736.673583984375 C1228,736.673583984375 1197,700.1784057617188 1169,642.5543823242188 C1136.1939697265625,575.7216186523438 1111.927734375,479.32598876953125 1102,370.3599853515625 C1077,96.94000244140625 1133,-4 1266.152099609375,-4 C1427.6083984375,-4 1607.1151123046875,220.92921447753906 1698.771728515625,462.18878173828125 C1725.037353515625,531.3258056640625 1735,596.2349243164062 1735,641.593994140625 C1735,695.3764038085938 1718,736.673583984375 1670,736.673583984375 C1623,736.673583984375 1592,700.1784057617188 1564,642.5543823242188 C1531.1939697265625,575.7216186523438 1506.927734375,479.32598876953125 1497,370.3599853515625 C1472,96.94000244140625 1528,-4 1646.906005859375,-4 C1765.623779296875,-4 1830.114990234375,99.48485565185547 1868.77880859375,209.3712158203125 C1907,318 1954,385 2052,385 C2133,385 2197,325 2197,212 C2197,87 2115.90087890625,-7 2013.41845703125,-8 C1923.234130859375,-9 1864,64 1870,174 C1877,296 1951,385 2048,385 C2104,385 2151.03564453125,360.1071472167969 2188,333 C2288.21435546875,259.8928527832031 2365.4287109375,305.0714416503906 2395,377.3571472167969"
                ),
                viewBoxHeight = 728.156f,
                viewBoxMinX = -109f,
                viewBoxWidth = 2504f
            ),

            // 2. bonjour (French) — viewBox="-92 -314 3754 1064"
            HelloLanguage(
                label = "bonjour",
                paths = listOf(
                    "M-92.45359802246094,74.660400390625 C71.46769714355469,131.75210571289062 182.9971923828125,246.96530151367188 298.6625061035156,489.3955078125 C328.46258544921875,551.8552856445312 338.9466857910156,609.31787109375 341.9779968261719,653.3721923828125 C345,709.4398803710938 322,749.6199951171875 277,749.6199951171875 C232,749.6199951171875 202,717.280029296875 169,648.6799926757812 C130,566.3599853515625 107,468.3599853515625 95,370.3599853515625 C64,96.94000244140625 129,-4 238,-4 C328,-4 385,77 392,184 C396,274 358,344 290,377",
                    "M349.56304931640625,329.85150146484375 C450.0641784667969,205.14019775390625 613.7254028320312,294.54595947265625 689.689697265625,351.093994140625 C722.9686889648438,375.86700439453125 752.3222045898438,385 788,385 C867,385 929,325 929,212 C929,87 850,-7 750,-8 C662,-9 604,64 610,174 C617,296 689,385 784,385 C838,385 876,366 920,333 C1043.56982421875,240.5821990966797 1167.879638671875,285.28936767578125 1198,380",
                    "M1198,380 C1179,253 1163,128 1143,1",
                    "M1156.63037109375,90.95342254638672 C1184.15869140625,278.8768005371094 1268,388 1368,388 C1438,388 1474,340 1468,272 C1463,221 1446,162 1442,112 C1437,43 1469,-4 1538.50048828125,-4 C1642.873046875,-4 1747.0008544921875,127.54780578613281 1777.2442626953125,290.31475830078125 C1782.6065673828125,319.1737365722656 1790.415771484375,350.5982666015625 1794,381",
                    "M1794,381 C1775.3333740234375,222.66665649414062 1756.6666259765625,64.33331298828125 1738,-94 C1719.483642578125,-251.05780029296875 1673,-314 1603,-314 C1556,-314 1522,-284.0697937011719 1522,-236 C1522,-171.7718048095703 1570.7403564453125,-125.66320037841797 1679.189697265625,-92.1765365600586 C1866.2293701171875,-34.42295837402344 2002.8282470703125,75.93299865722656 2047.70703125,203.48324584960938 C2088,318 2135,385 2233,385 C2314,385 2378,325 2378,212 C2378,87 2296.90087890625,-7 2194.41845703125,-8 C2104.234130859375,-9 2045,64 2051,174 C2058,296 2132,385 2229,385 C2285,385 2324,366 2369,333 C2506.075439453125,233.00228881835938 2619.94677734375,280.4041442871094 2660.395263671875,379.2782897949219",
                    "M2660.395263671875,379.2782897949219 C2643.1162109375,306.2179870605469 2631.5341796875,247 2625.38037109375,203 C2620.2685546875,171 2617.066650390625,149 2615.81884765625,122 C2614.167236328125,50.9989013671875 2653.994384765625,0 2731,0 C2843,0 2898.651123046875,99.65470123291016 2932.39306640625,263.0910949707031 C2940.46337890625,302.1805725097656 2949.257080078125,339.4844665527344 2956.37158203125,378.75750732421875",
                    "M2956.37158203125,378.75750732421875 C2931.37158203125,240.75750732421875 2911,158 2911,112 C2911,43 2938,-4 3011.62255859375,-4 C3130.4892578125,-4 3231.96728515625,164.47109985351562 3283,393",
                    "M3275.099853515625,359.0414733886719 C3399.099609375,353.0414733886719 3454,328 3454,269 C3454,228 3434,165 3428,119 C3417,39 3446.5087890625,-5 3510,-5 C3587.20361328125,-5 3640.627685546875,46.289573669433594 3661.471923828125,98.4000015258789"
                ),
                viewBoxHeight = 435.62f,
                viewBoxMinX = -92f,
                viewBoxWidth = 3754f
            ),

            // 3. halo (Indonesian) — viewBox="-87 -9 2234 746"
            HelloLanguage(
                label = "halo",
                paths = listOf(
                    "M-87.4115982055664,84.49829864501953 C23.391000747680664,149.7259063720703 119.25160217285156,231.5644073486328 226.2100067138672,376.3699951171875 C301.2781066894531,478.00079345703125 337.9945068359375,569.3778076171875 340,642.1939697265625 C341,696.1920166015625 315,737.156005859375 266,737.156005859375 C212,737.156005859375 178,696.1920166015625 157,602.1610107421875 C134,498.82000732421875 117,380.239990234375 74,0",
                    "M78.21453094482422,37.160953521728516 C99.42868041992188,223.645263671875 184,372 291,372 C355,372 395.6745910644531,321 384.1253967285156,248 C377.6238098144531,205 365.8778076171875,155 359.16668701171875,106 C351.0015869140625,44 374.3254089355469,-4 446.55615234375,-4 C548.778076171875,-4 610.0203857421875,106.56600952148438 643.21826171875,222.95718383789062",
                    "M958.600341796875,312.42718505859375 C938.99609375,357.5426940917969 897.3255004882812,388 830.994140625,388 C720.9940795898438,388 638.3248291015625,278 632.8975830078125,160 C628.167724609375,52 678.0023803710938,-8.710000038146973 748.984375,-8 C849.735595703125,-6.992199897766113 923.7940063476562,91.9677963256836 956.852294921875,301.3554992675781 C960.9309692382812,327.1893615722656 965.15771484375,354.1661376953125 969.2363891601562,380",
                    "M969.2363891601562,380 C965.107177734375,353.7950439453125 960.97802734375,327.5900573730469 956.8487548828125,301.3851013183594 C938.788818359375,186.7725067138672 930.4560546875,141.55520629882812 931.3510131835938,112 C933.440185546875,43 958.25439453125,-4 1036.7869873046875,-4 C1172.917724609375,-4 1360.622802734375,224.89764404296875 1450.9345703125,462.61773681640625 C1477.037353515625,531.3258056640625 1487,596.2349243164062 1487,641.593994140625 C1487,695.3764038085938 1470,736.673583984375 1422,736.673583984375 C1375,736.673583984375 1344,700.1784057617188 1316,642.5543823242188 C1283.1939697265625,575.7216186523438 1258.927734375,479.32598876953125 1249,370.3599853515625 C1224,96.94000244140625 1280,-4 1398.906005859375,-4 C1517.623779296875,-4 1582.114990234375,99.48485565185547 1620.77880859375,209.3712158203125 C1659,318 1706,385 1804,385 C1885,385 1949,325 1949,212 C1949,87 1867.90087890625,-7 1765.41845703125,-8 C1675.234130859375,-9 1616,64 1622,174 C1629,296 1703,385 1800,385 C1856,385 1903.03564453125,360.1071472167969 1940,333 C2040.21435546875,259.8928527832031 2117.4287109375,305.0714416503906 2147,377.3571472167969"
                ),
                viewBoxHeight = 728.156f,
                viewBoxMinX = -87f,
                viewBoxWidth = 2234f
            ),

        )
    }

    // ─── ANIMATION STATE ──────────────────────────────────────────────────────
    var currentGreetingIndex by remember { mutableStateOf(0) }
    val writingProgress = remember { Animatable(0f) }
    var wordAlpha by remember { mutableStateOf(1f) }
    var isLogoPhase by remember { mutableStateOf(false) }

    val wordAnimatedAlpha by animateFloatAsState(
        targetValue = wordAlpha,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "WordAlpha"
    )

    LaunchedEffect(Unit) {
        delay(300)

        for (i in greetings.indices) {
            currentGreetingIndex = i
            wordAlpha = 1f
            writingProgress.snapTo(0f)

            // Stroke drawing — time proportional to path count
            val durationMs = (1800 + greetings[i].paths.size * 120).coerceIn(1800, 3200)
            writingProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMs, easing = FastOutSlowInEasing)
            )

            // Hold so user can read it
            delay(900)

            // Fade out before next
            wordAlpha = 0f
            delay(550)
        }

        // Dissolve into logo
        delay(100)
        isLogoPhase = true
        delay(2200)

        val isLoggedIn = userProfile?.isLoggedIn ?: false
        if (isLoggedIn) onNavigateToHome() else onNavigateToAuth()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground),
        contentAlignment = Alignment.Center
    ) {
        // ── GREETING CANVAS ───────────────────────────────────────────────────
        AnimatedVisibility(
            visible = !isLogoPhase,
            enter = fadeIn(tween(400)),
            exit = fadeOut(tween(900, easing = FastOutSlowInEasing))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .alpha(wordAnimatedAlpha),                contentAlignment = Alignment.Center
            ) {
                val greeting = greetings[currentGreetingIndex]

                // Build Android paths from SVG data (recomputed when language changes)
                val nativePaths = remember(currentGreetingIndex) {
                    val parser = PathParser()
                    val flipMatrix = Matrix().apply {
                        postScale(1f, -1f)
                        postTranslate(0f, greeting.viewBoxHeight)
                    }
                    greeting.paths.map { svgData ->
                        parser.parsePathString(svgData).toPath().asAndroidPath().also { p ->
                            if (greeting.flipY) p.transform(flipMatrix)
                        }
                    }
                }

                val measures = remember(currentGreetingIndex) {
                    nativePaths.map { PathMeasure(it, false) }
                }
                val lengths = remember(currentGreetingIndex) { measures.map { it.length } }
                val totalLength = remember(currentGreetingIndex) { lengths.sum() }

                val dstPaths = remember(currentGreetingIndex) {
                    nativePaths.map { android.graphics.Path() }
                }

                androidx.compose.foundation.Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)  // Smaller canvas height
                ) {
                    val progress = writingProgress.value
                    if (totalLength <= 0f || progress <= 0f) return@Canvas

                    // Scale to fit the canvas — SMALLER factor (0.78 instead of 0.90)
                    val vbWidth = greeting.viewBoxWidth
                    val vbHeight = greeting.viewBoxHeight
                    val scaleFactor = minOf(
                        size.width / (vbWidth + 150f),
                        size.height / (vbHeight + 60f)
                    ) * 0.78f

                    val offsetX = (size.width - vbWidth * scaleFactor) / 2f - greeting.viewBoxMinX * scaleFactor
                    val offsetY = (size.height - vbHeight * scaleFactor) / 2f

                    withTransform({
                        translate(left = offsetX, top = offsetY)
                        scale(scale = scaleFactor, pivot = Offset.Zero)
                    }) {
                        val inkColor = Color(0xFF1E3A1E)
                        val strokeStyle = Stroke(
                            width = 50f,  // Thinner stroke
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )

                        var drawnLength = 0f
                        val targetLength = totalLength * progress

                        for (i in nativePaths.indices) {
                            val segStart = drawnLength
                            val segEnd = drawnLength + lengths[i]

                            if (segStart >= targetLength) break

                            dstPaths[i].reset()
                            val segProgress = ((targetLength - segStart) / lengths[i]).coerceIn(0f, 1f)
                            measures[i].getSegment(0f, lengths[i] * segProgress, dstPaths[i], true)

                            drawPath(
                                path = dstPaths[i].asComposePath(),
                                color = inkColor,
                                style = strokeStyle
                            )

                            // Ink nib glow at the active tip of the last drawing stroke
                            if (segProgress in 0.02f..0.98f && targetLength < segEnd) {
                                val pos = FloatArray(2)
                                measures[i].getPosTan(lengths[i] * segProgress, pos, null)
                                drawCircle(
                                    color = Color(0xFF4A6644).copy(alpha = 0.4f),
                                    radius = 28f,
                                    center = Offset(pos[0], pos[1])
                                )
                            }

                            drawnLength += lengths[i]
                        }
                    }
                }
            }
        }

        // ── PLANTYCARE LOGO PHASE ─────────────────────────────────────────────
        AnimatedVisibility(
            visible = isLogoPhase,
            enter = fadeIn(tween(900, easing = FastOutSlowInEasing)),
            exit = fadeOut(tween(500))
        ) {
            val logoScale by animateFloatAsState(
                targetValue = if (isLogoPhase) 1f else 0.92f,
                animationSpec = tween(900, easing = FastOutSlowInEasing),
                label = "LogoScale"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.scale(logoScale)
            ) {
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.White)
                        .border(1.2.dp, BorderSubtle, RoundedCornerShape(32.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = R.drawable.logo_plantycare,
                        contentDescription = "PlantyCare Official Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "plantycare",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = SagePrimary,
                    fontSize = 32.sp,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Smart Indoor Plant & Environment Monitor",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
        }
    }
}


