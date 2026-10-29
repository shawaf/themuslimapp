package me.shawaf.themuslimapp.features.home.presentation
import androidx.compose.material3.MaterialTheme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.features.home.presentation.viewmodel.HomeIntent
import me.shawaf.themuslimapp.features.home.presentation.viewmodel.HomeViewModel
import me.shawaf.themuslimapp.features.home.presentation.viewmodel.PrayerTimeUi
import me.shawaf.themuslimapp.ui.navigation.NoorRoutes
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigate: (String) -> Unit,
    onSettings: () -> Unit,
) {
    val state by homeViewModel.viewState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 18.dp, bottom = 24.dp),
    ) {
        GreetingHeader(location = state.location, hijriDate = state.hijriDate, onSettings = onSettings)

        Spacer(Modifier.height(16.dp))

        HeroCard(
            nextPrayerName = state.nextPrayerName,
            nextPrayerTime = state.nextPrayerTime,
            countdown = state.countdown,
            sunProgress = state.sunProgress,
            sunriseTime = state.sunriseTime,
            sunsetTime = state.sunsetTime,
            prayers = state.prayers,
            isAzanPlaying = state.isAzanPlaying,
            onToggleAzan = { homeViewModel.handleIntent(HomeIntent.ToggleAzan) },
        )

        Spacer(Modifier.height(22.dp))

        Text("Explore", color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        FeatureGrid(onNavigate)

        Spacer(Modifier.height(18.dp))

        DuaCard(arabic = state.duaArabic, translation = state.duaTranslation)
    }
}

@Composable
private fun GreetingHeader(location: String, hijriDate: String, onSettings: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text("Assalamu Alaikum", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            Text("Peace be upon you", color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            if (hijriDate.isNotEmpty()) {
                Text(
                    hijriDate,
                    color = NoorColors.Emerald,
                    fontSize = 13.sp,
                    fontFamily = AmiriFontFamily,
                    modifier = Modifier.padding(top = 3.dp),
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 7.dp),
            ) {
                Icon(Icons.Default.Place, null, tint = NoorColors.Gold, modifier = Modifier.size(14.dp))
                Spacer(Modifier.size(5.dp))
                Text(location, color = NoorColors.Emerald, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.size(8.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(onClick = onSettings),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Settings, "Settings", tint = NoorColors.Emerald, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun HeroCard(
    nextPrayerName: String,
    nextPrayerTime: String,
    countdown: String,
    sunProgress: Float,
    sunriseTime: String,
    sunsetTime: String,
    prayers: List<PrayerTimeUi>,
    isAzanPlaying: Boolean,
    onToggleAzan: () -> Unit,
) {
    var started by remember { mutableStateOf(false) }
    val animProgress by animateFloatAsState(
        targetValue = if (started) sunProgress else 0f,
        animationSpec = tween(durationMillis = 1400),
        label = "sunArc",
    )
    LaunchedEffect(sunProgress) { started = true }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(14.dp, RoundedCornerShape(28.dp), clip = false)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark)),
            )
            .padding(18.dp),
    ) {
        // Sun arc
        Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
            Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                val cx = size.width / 2f
                val baseY = size.height - 20.dp.toPx()
                val rx = size.width / 2f - 10.dp.toPx()
                val ry = baseY - 14.dp.toPx()
                val topLeft = androidx.compose.ui.geometry.Offset(cx - rx, baseY - ry)
                val arcSize = androidx.compose.ui.geometry.Size(rx * 2f, ry * 2f)
                drawArc(
                    color = Color.White.copy(alpha = 0.3f),
                    startAngle = 180f, sweepAngle = 180f, useCenter = false,
                    topLeft = topLeft, size = arcSize,
                    style = Stroke(2.dp.toPx(), cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 6.dp.toPx()))),
                )
                drawArc(
                    color = NoorColors.GoldLight,
                    startAngle = 180f, sweepAngle = 180f * animProgress.coerceIn(0f, 1f), useCenter = false,
                    topLeft = topLeft, size = arcSize,
                    style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round),
                )
                val theta = PI * (1f + animProgress.coerceIn(0f, 1f))
                val sunX = cx + rx * cos(theta).toFloat()
                val sunY = baseY + ry * sin(theta).toFloat()
                drawCircle(Color(0xFFFFD572).copy(alpha = 0.3f), 12.dp.toPx(), androidx.compose.ui.geometry.Offset(sunX, sunY))
                drawCircle(Color(0xFFFFD572), 6.5.dp.toPx(), androidx.compose.ui.geometry.Offset(sunX, sunY))
            }
            Column(
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 44.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("NEXT · ${nextPrayerName.uppercase()} · $countdown",
                    color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Text(nextPrayerTime, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(NoorColors.GoldLight.copy(alpha = if (isAzanPlaying) 0.3f else 0.16f))
                        .clickable(onClick = onToggleAzan)
                        .padding(horizontal = 15.dp, vertical = 7.dp),
                ) {
                    Icon(Icons.Default.PlayArrow, null, tint = NoorColors.GoldLight, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.size(6.dp))
                    Text(if (isAzanPlaying) "Playing Adhan…" else "Play Adhan",
                        color = NoorColors.GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            EndLabel(sunriseTime, "Sunrise", Modifier.align(Alignment.BottomStart))
            EndLabel(sunsetTime, "Sunset", Modifier.align(Alignment.BottomEnd))
        }

        Spacer(Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            prayers.forEach { p ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(p.name, color = if (p.isNext) NoorColors.GoldLight else Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(3.dp))
                    if (p.isNext) {
                        Text(p.time, color = NoorColors.EmeraldDark, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(NoorColors.GoldLight)
                                .padding(horizontal = 7.dp, vertical = 2.dp))
                    } else {
                        Text(p.time, color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun EndLabel(time: String, label: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(time, color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
    }
}

private data class FeatureTile(val label: String, val icon: ImageVector, val route: String)

@Composable
private fun FeatureGrid(onNavigate: (String) -> Unit) {
    val tiles = listOf(
        FeatureTile("Sebha", Icons.Default.Refresh, NoorRoutes.SEBHA),
        FeatureTile("Prayer", Icons.Default.Notifications, NoorRoutes.PRAYER),
        FeatureTile("Qibla", Icons.Default.Place, NoorRoutes.QIBLA),
        FeatureTile("Duas", Icons.Default.FavoriteBorder, NoorRoutes.DUAS),
        FeatureTile("Hadith", Icons.AutoMirrored.Filled.List, NoorRoutes.HADITH),
        FeatureTile("Calendar", Icons.Default.DateRange, NoorRoutes.CALENDAR),
    )
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        tiles.chunked(3).forEach { rowTiles ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowTiles.forEach { tile ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(6.dp, RoundedCornerShape(20.dp), clip = false)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { onNavigate(tile.route) }
                            .padding(vertical = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(tile.icon, tile.label, tint = NoorColors.Emerald, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(tile.label, color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DuaCard(arabic: String, translation: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(listOf(NoorColors.EmeraldDark, NoorColors.Emerald)))
            .padding(18.dp),
    ) {
        Text("DUA OF THE DAY", color = NoorColors.GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Text(arabic, color = Color.White, fontSize = 22.sp, fontFamily = AmiriFontFamily,
            fontWeight = FontWeight.Bold, lineHeight = 38.sp, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Text(translation, color = Color.White.copy(alpha = 0.78f), fontSize = 12.sp)
    }
}
