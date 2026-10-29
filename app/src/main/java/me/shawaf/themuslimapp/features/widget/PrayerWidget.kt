package me.shawaf.themuslimapp.features.widget

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import me.shawaf.themuslimapp.ui.theme.NoorColors

/**
 * Home-screen widget showing today's 5 prayers, the next one highlighted in
 * gold, and a live countdown to the next salah — in the app's current language
 * (Arabic default, RTL). Reuses the app's prayer data layer via [PrayerWidgetData].
 */
class PrayerWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val state = PrayerWidgetData.load(context)
        provideContent { WidgetContent(context, state) }
    }
}

private val Emerald = NoorColors.Emerald
private val EmeraldDark = NoorColors.EmeraldDark
private val Gold = NoorColors.Gold
private val GoldLight = NoorColors.GoldLight
private val OnEmerald = Color(0xFFF4F1E9)
private val OnEmeraldDim = Color(0xFFB9CEC6)

@androidx.compose.runtime.Composable
private fun WidgetContent(context: Context, state: PrayerWidgetState) {
    val openApp = actionStartActivity(
        Intent().setComponent(
            ComponentName(context.packageName, "me.shawaf.themuslimapp.features.MainActivity"),
        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
    )

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Emerald)
            .cornerRadius(20.dp)
            .padding(14.dp)
            .clickable(openApp),
    ) {
        // Header: next prayer + countdown on one side, location on the other.
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = if (state.isArabic) "الصلاة القادمة" else "Next prayer",
                    style = TextStyle(color = ColorProvider(OnEmeraldDim), fontSize = 11.sp),
                )
                Text(
                    text = state.nextPrayerName,
                    style = TextStyle(
                        color = ColorProvider(GoldLight),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = state.countdown,
                    style = TextStyle(
                        color = ColorProvider(OnEmerald),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )
                Text(
                    text = state.locationLabel,
                    style = TextStyle(color = ColorProvider(OnEmeraldDim), fontSize = 11.sp),
                )
            }
        }

        Spacer(modifier = GlanceModifier.height(12.dp))

        // Row of the 5 daily prayers; next one highlighted in gold.
        // Reverse for Arabic so Fajr reads on the right (RTL).
        val ordered = if (state.isArabic) state.prayers.reversed() else state.prayers
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            ordered.forEachIndexed { index, prayer ->
                PrayerCell(prayer, modifier = GlanceModifier.defaultWeight())
                if (index != ordered.lastIndex) {
                    Spacer(modifier = GlanceModifier.width(6.dp))
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun PrayerCell(prayer: WidgetPrayer, modifier: GlanceModifier) {
    val bg = if (prayer.isNext) Gold else EmeraldDark
    val nameColor = if (prayer.isNext) EmeraldDark else OnEmerald
    val timeColor = if (prayer.isNext) EmeraldDark else OnEmeraldDim

    Box(
        modifier = modifier
            .background(bg)
            .cornerRadius(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = GlanceModifier.padding(vertical = 8.dp, horizontal = 2.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = prayer.name,
                maxLines = 1,
                style = TextStyle(
                    color = ColorProvider(nameColor),
                    fontSize = 12.sp,
                    fontWeight = if (prayer.isNext) FontWeight.Bold else FontWeight.Medium,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            Text(
                text = prayer.time,
                maxLines = 1,
                style = TextStyle(
                    color = ColorProvider(timeColor),
                    fontSize = 12.sp,
                    fontWeight = if (prayer.isNext) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}
