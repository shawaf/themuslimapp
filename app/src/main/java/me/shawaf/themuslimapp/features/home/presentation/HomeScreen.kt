package me.shawaf.themuslimapp.features.home.presentation

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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.features.home.presentation.viewmodel.HomeIntent
import me.shawaf.themuslimapp.features.home.presentation.viewmodel.HomeViewModel
import me.shawaf.themuslimapp.features.home.presentation.viewmodel.PrayerTimeUi
import me.shawaf.themuslimapp.ui.navigation.NoorRoutes
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors
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
            .verticalScroll(rememberScrollState()),
    ) {
        Hero(
            location = state.location,
            nextPrayerName = state.nextPrayerName,
            nextPrayerTime = state.nextPrayerTime,
            countdown = state.countdown,
            sunProgress = state.sunProgress,
            sunriseTime = state.sunriseTime,
            sunsetTime = state.sunsetTime,
            prayers = state.prayers,
            isAzanPlaying = state.isAzanPlaying,
            onToggleAzan = { homeViewModel.handleIntent(HomeIntent.ToggleAzan) },
            onSettings = onSettings,
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 18.dp, bottom = 24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Explore", color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                if (state.hijriDate.isNotEmpty()) {
                    Text(state.hijriDate, color = NoorColors.Emerald, fontSize = 13.sp, fontFamily = AmiriFontFamily)
                }
            }
            Spacer(Modifier.height(12.dp))
            FeatureGrid(onNavigate)
            Spacer(Modifier.height(18.dp))
            DuaCard(arabic = state.duaArabic, translation = state.duaTranslation)
        }
    }
}

@Composable
private fun Hero(
    location: String,
    nextPrayerName: String,
    nextPrayerTime: String,
    countdown: String,
    sunProgress: Float,
    sunriseTime: String,
    sunsetTime: String,
    prayers: List<PrayerTimeUi>,
    isAzanPlaying: Boolean,
    onToggleAzan: () -> Unit,
    onSettings: () -> Unit,
) {
    var started by remember { mutableStateOf(false) }
    val animProgress by animateFloatAsState(
        targetValue = if (started) sunProgress else 0f,
        animationSpec = tween(1400),
        label = "sunArc",
    )
    LaunchedEffect(sunProgress) { started = true }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
            .background(Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark))),
    ) {
        // Arabesque motif
        Canvas(modifier = Modifier.matchParentSize()) {
            val tile = 46f * density
            val color = NoorColors.GoldLight.copy(alpha = 0.10f)
            var row = 0
            var y = -tile
            while (y < size.height) {
                var x = if (row % 2 == 0) -tile else -tile / 2f
                while (x < size.width + tile) {
                    val cx = x + tile / 2f
                    val cy = y + tile / 2f
                    val p = androidx.compose.ui.graphics.Path().apply {
                        moveTo(cx, y); lineTo(x + tile, cy); lineTo(cx, y + tile); lineTo(x, cy); close()
                    }
                    drawPath(p, color, style = Stroke(1f))
                    x += tile
                }
                y += tile; row++
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .padding(top = 20.dp, bottom = 24.dp),
        ) {
            // Greeting row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text("Assalamu Alaikum", color = Color(0xFFCFE3DC), fontSize = 12.5.sp)
                    Text("Peace be upon you", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(horizontal = 11.dp, vertical = 7.dp),
                    ) {
                        Icon(Icons.Default.Place, null, tint = NoorColors.GoldLight, modifier = Modifier.size(13.dp))
                        Spacer(Modifier.size(5.dp))
                        Text(location, color = Color(0xFFEAF3EF), fontSize = 11.5.sp)
                    }
                    Spacer(Modifier.size(8.dp))
                    Box(
                        modifier = Modifier.size(34.dp).clip(RoundedCornerShape(17.dp))
                            .background(Color.White.copy(alpha = 0.12f)).clickable(onClick = onSettings),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.Settings, "Settings", tint = NoorColors.GoldLight, modifier = Modifier.size(17.dp))
                    }
                }
            }

            // Sun arc + next prayer
            Box(modifier = Modifier.fillMaxWidth().height(160.dp).padding(top = 10.dp)) {
                Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                    val cx = size.width / 2f
                    val baseY = size.height - 20.dp.toPx()
                    val rx = size.width / 2f - 10.dp.toPx()
                    val ry = baseY - 14.dp.toPx()
                    val topLeft = Offset(cx - rx, baseY - ry)
                    val arcSize = Size(rx * 2f, ry * 2f)
                    drawArc(
                        color = Color.White.copy(alpha = 0.3f), startAngle = 180f, sweepAngle = 180f, useCenter = false,
                        topLeft = topLeft, size = arcSize,
                        style = Stroke(2.dp.toPx(), cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 6.dp.toPx()))),
                    )
                    drawArc(
                        color = NoorColors.GoldLight, startAngle = 180f, sweepAngle = 180f * animProgress.coerceIn(0f, 1f),
                        useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round),
                    )
                    val theta = PI * (1f + animProgress.coerceIn(0f, 1f))
                    val sunX = cx + rx * cos(theta).toFloat()
                    val sunY = baseY + ry * sin(theta).toFloat()
                    drawCircle(Color(0xFFFFD572).copy(alpha = 0.3f), 12.dp.toPx(), Offset(sunX, sunY))
                    drawCircle(Color(0xFFFFD572), 6.5.dp.toPx(), Offset(sunX, sunY))
                }
                Column(modifier = Modifier.align(Alignment.TopCenter).padding(top = 42.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("NEXT · ${nextPrayerName.uppercase()} · $countdown", color = Color(0xFF9FC6BA), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text(nextPrayerTime, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clip(RoundedCornerShape(22.dp))
                            .background(NoorColors.GoldLight.copy(alpha = if (isAzanPlaying) 0.3f else 0.16f))
                            .clickable(onClick = onToggleAzan).padding(horizontal = 15.dp, vertical = 7.dp),
                    ) {
                        Icon(Icons.Default.PlayArrow, null, tint = NoorColors.GoldLight, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.size(6.dp))
                        Text(if (isAzanPlaying) "Playing Adhan…" else "Play Adhan", color = NoorColors.GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                EndLabel(sunriseTime, "Sunrise", Modifier.align(Alignment.BottomStart))
                EndLabel(sunsetTime, "Sunset", Modifier.align(Alignment.BottomEnd))
            }

            // Timeline
            Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                prayers.forEach { p ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(p.name, color = if (p.isNext) NoorColors.GoldLight else Color(0xFF8FB9AC), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(3.dp))
                        if (p.isNext) {
                            Text(p.time, color = NoorColors.EmeraldDark, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                                modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(NoorColors.GoldLight).padding(horizontal = 7.dp, vertical = 2.dp))
                        } else {
                            Text(p.time, color = Color(0xFFCFE3DC), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EndLabel(time: String, label: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(time, color = Color(0xFF9FC6BA), fontSize = 10.sp)
        Text(label, color = Color(0xFF7FA79A), fontSize = 10.sp)
    }
}

private data class FeatureTile(val label: String, val iconRes: Int, val route: String)

@Composable
private fun FeatureGrid(onNavigate: (String) -> Unit) {
    val tiles = listOf(
        FeatureTile("Sebha", R.drawable.ic_feat_sebha, NoorRoutes.SEBHA),
        FeatureTile("Prayer", R.drawable.ic_feat_prayer, NoorRoutes.PRAYER),
        FeatureTile("Qibla", R.drawable.ic_feat_qibla, NoorRoutes.QIBLA),
        FeatureTile("Duas", R.drawable.ic_feat_duas, NoorRoutes.DUAS),
        FeatureTile("Hadith", R.drawable.ic_feat_hadith, NoorRoutes.HADITH),
        FeatureTile("Calendar", R.drawable.ic_feat_calendar, NoorRoutes.CALENDAR),
    )
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        tiles.chunked(3).forEach { rowTiles ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowTiles.forEach { tile ->
                    Column(
                        modifier = Modifier.weight(1f)
                            .shadow(6.dp, RoundedCornerShape(20.dp), clip = false)
                            .clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surface)
                            .clickable { onNavigate(tile.route) }.padding(vertical = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(painterResource(tile.iconRes), tile.label, tint = NoorColors.Emerald, modifier = Modifier.size(22.dp))
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
        modifier = Modifier.fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(listOf(NoorColors.EmeraldDark, NoorColors.Emerald)))
            .padding(18.dp),
    ) {
        Text("DUA OF THE DAY", color = NoorColors.GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Text(arabic, color = Color.White, fontSize = 22.sp, fontFamily = AmiriFontFamily, fontWeight = FontWeight.Bold, lineHeight = 38.sp, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Text(translation, color = Color.White.copy(alpha = 0.78f), fontSize = 12.sp)
    }
}
