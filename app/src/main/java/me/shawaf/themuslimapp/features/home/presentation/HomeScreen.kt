package me.shawaf.themuslimapp.features.home.presentation

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val Gold = Color(0xFFE7CE9A)
private val Sun = Color(0xFFFFD572)

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigate: (String) -> Unit,
) {
    val state by homeViewModel.viewState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 24.dp),
    ) {
        GreetingHeader(location = state.location, hijriDate = state.hijriDate)

        Spacer(Modifier.height(8.dp))

        SunArcCard(
            nextPrayerName = state.nextPrayerName,
            nextPrayerTime = state.nextPrayerTime,
            countdown = state.countdown,
            sunProgress = state.sunProgress,
            sunriseTime = state.sunriseTime,
            sunsetTime = state.sunsetTime,
            isAzanPlaying = state.isAzanPlaying,
            onToggleAzan = { homeViewModel.handleIntent(HomeIntent.ToggleAzan) },
        )

        Spacer(Modifier.height(12.dp))

        PrayerTimelineRow(state.prayers)

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Explore",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        FeatureGrid(onNavigate)

        Spacer(Modifier.height(18.dp))

        DuaCard(arabic = state.duaArabic, translation = state.duaTranslation)
    }
}

@Composable
private fun GreetingHeader(location: String, hijriDate: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text("Assalamu Alaikum", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            Text(
                "Peace be upon you",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .padding(horizontal = 12.dp, vertical = 7.dp),
        ) {
            Icon(
                Icons.Default.Place,
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.size(5.dp))
            Text(location, color = Color.White, fontSize = 12.sp)
        }
    }
    if (hijriDate.isNotEmpty()) {
        Text(
            hijriDate,
            color = Gold,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}

@Composable
private fun SunArcCard(
    nextPrayerName: String,
    nextPrayerTime: String,
    countdown: String,
    sunProgress: Float,
    sunriseTime: String,
    sunsetTime: String,
    isAzanPlaying: Boolean,
    onToggleAzan: () -> Unit,
) {
    val primary = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.55f))
            .padding(16.dp),
    ) {
        // The arc itself
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
        ) {
            val pad = 26.dp.toPx()
            val cx = size.width / 2f
            val baseY = size.height - 18.dp.toPx()
            val rx = size.width / 2f - pad
            val ry = baseY - 18.dp.toPx()
            val topLeft = androidx.compose.ui.geometry.Offset(cx - rx, baseY - ry)
            val arcSize = androidx.compose.ui.geometry.Size(rx * 2f, ry * 2f)

            // Full dome (left -> top -> right)
            drawArc(
                color = Color.White.copy(alpha = 0.28f),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(
                    width = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(2.dp.toPx(), 6.dp.toPx()),
                    ),
                ),
            )
            // Travelled portion (sunrise -> now)
            drawArc(
                color = Gold,
                startAngle = 180f,
                sweepAngle = 180f * sunProgress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round),
            )
            // Sun marker
            val theta = PI * (1f + sunProgress.coerceIn(0f, 1f))
            val sunX = cx + rx * cos(theta).toFloat()
            val sunY = baseY + ry * sin(theta).toFloat()
            drawCircle(
                color = Sun.copy(alpha = 0.3f),
                radius = 12.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(sunX, sunY),
            )
            drawCircle(
                color = Sun,
                radius = 6.5.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(sunX, sunY),
            )
        }

        // Centre content under the dome
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 42.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Next · $nextPrayerName · $countdown".uppercase(),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                nextPrayerTime,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .background(Gold.copy(alpha = if (isAzanPlaying) 0.28f else 0.14f))
                    .clickable(onClick = onToggleAzan)
                    .padding(horizontal = 15.dp, vertical = 7.dp),
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Gold,
                    modifier = Modifier.size(15.dp),
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    if (isAzanPlaying) "Playing Adhan…" else "Play Adhan",
                    color = Gold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        // Sunrise / sunset labels at the arc ends
        EndLabel(
            time = sunriseTime,
            label = "Sunrise",
            modifier = Modifier.align(Alignment.BottomStart),
        )
        EndLabel(
            time = sunsetTime,
            label = "Sunset",
            modifier = Modifier.align(Alignment.BottomEnd),
        )
    }
}

@Composable
private fun EndLabel(time: String, label: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(time, color = Color.White.copy(alpha = 0.75f), fontSize = 10.sp)
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
    }
}

@Composable
private fun PrayerTimelineRow(prayers: List<PrayerTimeUi>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        prayers.forEach { prayer ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    prayer.name,
                    color = if (prayer.isNext) Gold else Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(3.dp))
                if (prayer.isNext) {
                    Text(
                        prayer.time,
                        color = Color(0xFF0A3D33),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Gold)
                            .padding(horizontal = 7.dp, vertical = 2.dp),
                    )
                } else {
                    Text(
                        prayer.time,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowTiles.forEach { tile ->
                    FeatureTileCard(
                        tile = tile,
                        onClick = { onNavigate(tile.route) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureTileCard(
    tile: FeatureTile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                tile.icon,
                contentDescription = tile.label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(tile.label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun DuaCard(arabic: String, translation: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.22f))
            .padding(18.dp),
    ) {
        Text(
            "DUA OF THE DAY",
            color = Gold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            arabic,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(6.dp))
        Text(translation, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
    }
}
