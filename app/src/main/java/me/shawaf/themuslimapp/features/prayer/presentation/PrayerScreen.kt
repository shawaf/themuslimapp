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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerIntent
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerSlotUi
import me.shawaf.themuslimapp.features.prayer.presentation.viewmodel.PrayerViewModel

private val Gold = Color(0xFFE7CE9A)

@Composable
fun PrayerScreen(prayerViewModel: PrayerViewModel) {
    val state by prayerViewModel.viewState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        // Reload with whatever was granted (location may now be available).
        prayerViewModel.handleIntent(PrayerIntent.Load)
    }

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
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 20.dp),
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Prayer Times",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.weight(1f))
            Icon(
                Icons.Default.Place,
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(15.dp),
            )
            Spacer(Modifier.size(4.dp))
            Text(
                state.locationLabel + if (state.usingDefaultLocation) " · default" else "",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
            )
        }

        Spacer(Modifier.height(16.dp))

        // Up next
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.22f))
                .padding(18.dp),
        ) {
            Text(
                "UP NEXT",
                color = Gold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${state.nextNameEn} · ${state.nextTimeText}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.weight(1f))
                if (state.countdown.isNotEmpty()) {
                    Text(
                        state.countdown,
                        color = Gold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Prayer list
        Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            state.slots.forEach { slot -> PrayerRow(slot) { prayerViewModel.handleIntent(PrayerIntent.ToggleAzan(slot.key)) } }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Calculation: ${state.calcMethodLabel}",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp,
        )
    }
}

@Composable
private fun PrayerRow(slot: PrayerSlotUi, onToggleAzan: () -> Unit) {
    val background = if (slot.isNext) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                slot.nameEn,
                color = if (slot.isNext) Gold else Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(slot.nameAr, color = Color.White.copy(alpha = 0.55f), fontSize = 12.sp)
        }
        Spacer(Modifier.weight(1f))
        Text(
            slot.timeText,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )
        if (slot.canAzan) {
            Spacer(Modifier.size(10.dp))
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (slot.azanOn) Gold.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.06f),
                    )
                    .clickable(onClick = onToggleAzan),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = if (slot.azanOn) "Azan on" else "Azan off",
                    tint = if (slot.azanOn) Gold else Color.White.copy(alpha = 0.35f),
                    modifier = Modifier.size(17.dp),
                )
            }
        } else {
            Spacer(Modifier.size(44.dp))
        }
    }
}
