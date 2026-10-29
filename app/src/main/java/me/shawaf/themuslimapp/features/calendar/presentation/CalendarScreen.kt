package me.shawaf.themuslimapp.features.calendar.presentation

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.features.calendar.domain.model.CalendarDay
import me.shawaf.themuslimapp.features.calendar.domain.model.IslamicEvent
import me.shawaf.themuslimapp.features.calendar.presentation.viewmodel.CalendarViewModel
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors

@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel, onBack: () -> Unit) {
    val state by calendarViewModel.viewState.collectAsState()
    val month = state.month

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Header banner
        Box(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark))),
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val tile = 42f * density
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.14f)).clickable(onClick = onBack),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.size(12.dp))
                    Text("Islamic Calendar", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(14.dp))
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${month?.hijriMonthAr ?: ""} ${month?.hijriYear ?: ""}", color = NoorColors.GoldLight,
                        fontSize = 26.sp, fontFamily = AmiriFontFamily, fontWeight = FontWeight.Bold)
                    Text("${month?.hijriMonthEn ?: ""} ${month?.hijriYear ?: ""} · ${month?.gregorianLabel ?: ""}",
                        color = Color(0xFFCFE3DC), fontSize = 13.sp)
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 16.dp, bottom = 24.dp)) {
            if (month != null) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(20.dp), clip = false)
                        .clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("S", "M", "T", "W", "T", "F", "S").forEach { d ->
                            Text(d, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    month.days.chunked(7).forEach { week ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            week.forEach { day -> DayCell(day, Modifier.weight(1f)) }
                            repeat(7 - week.size) { Box(Modifier.weight(1f)) }
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Text("Events this year", color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.events.forEach { event -> EventCard(event, highlight = event.isNext) }
            }
        }
    }
}

@Composable
private fun DayCell(day: CalendarDay, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(2.dp), contentAlignment = Alignment.Center) {
        if (day.day != null) {
            Box(
                modifier = Modifier.size(34.dp).clip(RoundedCornerShape(11.dp))
                    .background(if (day.isToday) NoorColors.Emerald else Color.Transparent),
                contentAlignment = Alignment.Center,
            ) {
                Text("${day.day}", color = if (day.isToday) Color.White else MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp, fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun EventCard(event: IslamicEvent, highlight: Boolean) {
    val isPast = event.daysAway < 0
    val dayColor = when {
        highlight -> NoorColors.GoldLight
        isPast -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> NoorColors.Gold
    }
    val nameColor = when {
        highlight -> Color.White
        isPast -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onBackground
    }
    val subColor = if (highlight) Color(0xFFBBD6CC) else MaterialTheme.colorScheme.onSurfaceVariant
    val awayText = when {
        event.daysAway == 0 -> "Today"
        event.daysAway > 0 -> "in ${event.daysAway}d"
        else -> "${-event.daysAway}d ago"
    }
    Row(
        modifier = Modifier.fillMaxWidth()
            .shadow(if (highlight) 10.dp else 3.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (highlight) Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark))
                else Brush.linearGradient(listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface)),
            )
            .padding(horizontal = 16.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
            Text("${event.hijriDay}", color = dayColor, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text(event.hijriMonthEn, color = subColor, fontSize = 9.5.sp, textAlign = TextAlign.Center, maxLines = 1)
        }
        Spacer(Modifier.size(12.dp))
        Box(modifier = Modifier.width(1.dp).height(34.dp)
            .background(if (highlight) NoorColors.GoldLight.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline))
        Spacer(Modifier.size(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(event.nameEn, color = nameColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(event.description, color = subColor, fontSize = 11.5.sp)
        }
        Text(awayText, color = dayColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
