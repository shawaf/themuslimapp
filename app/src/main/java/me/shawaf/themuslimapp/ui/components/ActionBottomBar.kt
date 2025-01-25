package me.shawaf.themuslimapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.data.local.ConfigModel

@Composable
fun ActionBottomBar(
    configModel: ConfigModel,
    onSwitchTheme: (() -> Unit)?,
    onNavigateToInfo: (() -> Unit)?,
    onNavigateToHistory: (() -> Unit)?,
    onToggleSoundEnabledState: (() -> Unit)?,
    onToggleVibrationEnabledState: (() -> Unit)?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Padding to create space around the BottomBar
            .clip(RoundedCornerShape(24.dp)) // Rounded corners
            .background(MaterialTheme.colorScheme.surface) // Background color
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // Inner padding for Row content
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButtonWithState(
                iconSrc = R.drawable.ic_sound_on,
                contentDesc = "Sound",
                state = configModel.soundEnabled
            ) { onToggleSoundEnabledState?.let { it() } }
            ActionButtonWithState(
                iconSrc = R.drawable.ic_vibration,
                contentDesc = "Vibration",
                state = configModel.vibrationEnabled
            ) { onToggleVibrationEnabledState?.let { it() } }
            ActionButton(
                iconSrc = R.drawable.ic_color_platte, contentDesc = "Color"
            ) { onSwitchTheme?.let { it() } }
            ActionButton(
                iconSrc = R.drawable.ic_chart_bar, contentDesc = "Save"
            ) { onNavigateToHistory?.let { it() } }
            ActionButton(
                iconSrc = R.drawable.ic_info, contentDesc = "Info"
            ) { onNavigateToInfo?.let { it() } }
        }
    }
}