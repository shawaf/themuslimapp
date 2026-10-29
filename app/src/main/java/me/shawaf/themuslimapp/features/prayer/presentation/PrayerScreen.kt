package me.shawaf.themuslimapp.features.prayer.presentation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerIntent
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerSlotUi
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerViewModel
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp).padding(top = 18.dp, bottom = 20.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Prayer Times", color = NoorColors.Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.Place, null, tint = NoorColors.Gold, modifier = Modifier.size(15.dp))
            Spacer(Modifier.size(4.dp))
            Text(state.locationLabel + if (state.usingDefaultLocation) " · default" else "",
                color = NoorColors.Muted, fontSize = 12.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Up next
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(22.dp), clip = false)
                .clip(RoundedCornerShape(22.dp))
                .background(Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark)))
                .padding(18.dp),
        ) {
            Text("UP NEXT", color = NoorColors.GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${state.nextNameEn} · ${state.nextTimeText}", color = Color.White,
                    fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                if (state.countdown.isNotEmpty()) {
                    Text(state.countdown, color = NoorColors.GoldLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            state.slots.forEach { slot ->
                PrayerRow(slot) { prayerViewModel.handleIntent(PrayerIntent.ToggleAzan(slot.key)) }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Calculation: ${state.calcMethodLabel}", color = NoorColors.Muted, fontSize = 11.sp)
    }
}

@Composable
private fun PrayerRow(slot: PrayerSlotUi, onToggleAzan: () -> Unit) {
    val isNext = slot.isNext
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isNext) 8.dp else 4.dp, RoundedCornerShape(16.dp), clip = false)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isNext) NoorColors.SurfaceTint else NoorColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(slot.nameEn, color = if (isNext) NoorColors.Emerald else NoorColors.Ink,
                fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(slot.nameAr, color = NoorColors.Muted, fontSize = 13.sp, fontFamily = AmiriFontFamily)
        }
        Spacer(Modifier.weight(1f))
        Text(slot.timeText, color = NoorColors.Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        if (slot.canAzan) {
            Spacer(Modifier.size(10.dp))
            Box(
                modifier = Modifier.size(34.dp).clip(RoundedCornerShape(12.dp))
                    .background(if (slot.azanOn) NoorColors.SurfaceTint else NoorColors.IvoryDim)
                    .clickable(onClick = onToggleAzan),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Notifications,
                    if (slot.azanOn) "Azan on" else "Azan off",
                    tint = if (slot.azanOn) NoorColors.Gold else NoorColors.Muted,
                    modifier = Modifier.size(17.dp))
            }
        } else {
            Spacer(Modifier.size(44.dp))
        }
    }
}
