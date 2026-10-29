package me.shawaf.themuslimapp.features.prayer.presentation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerIntent
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerSlotUi
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerViewModel
import me.shawaf.themuslimapp.ui.theme.NoorColors

private fun weatherIcon(key: String): Int = when (key) {
    "FAJR" -> R.drawable.ic_w_fajr
    "DHUHR" -> R.drawable.ic_w_dhuhr
    "ASR" -> R.drawable.ic_w_asr
    "MAGHRIB" -> R.drawable.ic_w_maghrib
    "ISHA" -> R.drawable.ic_w_isha
    else -> R.drawable.ic_w_dhuhr
}

@Composable
fun PrayerScreen(prayerViewModel: PrayerViewModel) {
    val state by prayerViewModel.viewState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { prayerViewModel.handleIntent(PrayerIntent.Load) }
    LaunchedEffect(Unit) {
        val permissions = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) add(Manifest.permission.POST_NOTIFICATIONS)
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Header banner
        Box(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark))),
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val tile = 40f * density
                val color = NoorColors.GoldLight.copy(alpha = 0.1f)
                var row = 0; var y = -tile
                while (y < size.height) {
                    var x = if (row % 2 == 0) -tile else -tile / 2f
                    while (x < size.width + tile) {
                        val cx = x + tile / 2f; val cy = y + tile / 2f
                        val p = Path().apply { moveTo(cx, y); lineTo(x + tile, cy); lineTo(cx, y + tile); lineTo(x, cy); close() }
                        drawPath(p, color, style = Stroke(1f)); x += tile
                    }
                    y += tile; row++
                }
            }
            Column(modifier = Modifier.padding(horizontal = 22.dp).padding(top = 22.dp, bottom = 22.dp)) {
                Text("Prayer Times", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(state.dateLabel, color = Color(0xFF9FC6BA), fontSize = 12.sp)
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(NoorColors.GoldLight.copy(alpha = 0.14f))
                        .border(1.dp, NoorColors.GoldLight.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text("UP NEXT", color = NoorColors.GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("${state.nextNameEn} · ${state.nextTimeText}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    if (state.countdown.isNotEmpty()) {
                        Text(
                            state.countdown.removePrefix("in "),
                            color = Color(0xFFF0DEB4), fontSize = 13.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.Black.copy(alpha = 0.18f))
                                .padding(horizontal = 12.dp, vertical = 7.dp),
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 16.dp, bottom = 20.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                state.slots.filter { it.canAzan }.forEach { slot ->
                    PrayerRow(slot) { prayerViewModel.handleIntent(PrayerIntent.ToggleAzan(slot.key)) }
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant).padding(horizontal = 16.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("🔔 Azan · ${state.calcMethodLabel}", color = MaterialTheme.colorScheme.onBackground, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun PrayerRow(slot: PrayerSlotUi, onToggleAzan: () -> Unit) {
    val isNext = slot.isNext
    Row(
        modifier = Modifier.fillMaxWidth()
            .shadow(if (isNext) 10.dp else 4.dp, RoundedCornerShape(16.dp), clip = false)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isNext) Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark))
                else Brush.linearGradient(listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface)),
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(weatherIcon(slot.key)),
                contentDescription = null,
                tint = if (isNext) NoorColors.GoldLight else NoorColors.Gold,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.size(12.dp))
            Column {
                Text(slot.nameEn, color = if (isNext) Color.White else MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp, fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold)
                if (isNext) Text("Current", color = Color(0xFFA9CCC0), fontSize = 11.sp)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(slot.timeText, color = if (isNext) Color.White else MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.size(12.dp))
            Icon(
                painterResource(R.drawable.ic_pray_fig),
                contentDescription = if (slot.azanOn) "Azan on" else "Azan off",
                tint = when {
                    isNext -> NoorColors.GoldLight
                    slot.azanOn -> NoorColors.Emerald
                    else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                },
                modifier = Modifier.size(18.dp).clickable(onClick = onToggleAzan),
            )
        }
    }
}
