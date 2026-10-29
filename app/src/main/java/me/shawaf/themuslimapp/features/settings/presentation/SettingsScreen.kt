package me.shawaf.themuslimapp.features.settings.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.ui.components.NoorScreenHeader
import me.shawaf.themuslimapp.ui.theme.NoorColors
import me.shawaf.themuslimapp.ui.viewmodel.ThemeViewModel
import me.shawaf.themuslimapp.utils.LocaleHelper

@Composable
fun SettingsScreen(themeViewModel: ThemeViewModel, onBack: (() -> Unit)? = null) {
    val mode by themeViewModel.themeMode.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 18.dp),
    ) {
        NoorScreenHeader(stringResource(R.string.settings), onBack)
        Spacer(Modifier.height(18.dp))

        SectionLabel(stringResource(R.string.appearance))
        Spacer(Modifier.height(10.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(18.dp), clip = false)
                .clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface),
        ) {
            ThemeRow(stringResource(R.string.theme_system), stringResource(R.string.theme_system_sub), "system", mode) { themeViewModel.setThemeMode("system") }
            Divider()
            ThemeRow(stringResource(R.string.theme_light), stringResource(R.string.theme_light_sub), "light", mode) { themeViewModel.setThemeMode("light") }
            Divider()
            ThemeRow(stringResource(R.string.theme_dark), stringResource(R.string.theme_dark_sub), "dark", mode) { themeViewModel.setThemeMode("dark") }
        }

        Spacer(Modifier.height(22.dp))
        SectionLabel(stringResource(R.string.language))
        Spacer(Modifier.height(10.dp))
        val context = LocalContext.current
        val currentLang = remember { LocaleHelper.savedLanguage(context) }
        Column(
            modifier = Modifier.fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(18.dp), clip = false)
                .clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface),
        ) {
            LanguageRow("العربية", stringResource(R.string.lang_arabic_sub), LocaleHelper.ARABIC, currentLang) {
                LocaleHelper.saveLanguage(context, LocaleHelper.ARABIC)
                (context as? Activity)?.recreate()
            }
            Divider()
            LanguageRow("English", stringResource(R.string.lang_english_sub), LocaleHelper.ENGLISH, currentLang) {
                LocaleHelper.saveLanguage(context, LocaleHelper.ENGLISH)
                (context as? Activity)?.recreate()
            }
        }

        Spacer(Modifier.height(22.dp))
        SectionLabel(stringResource(R.string.about))
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(18.dp), clip = false)
                .clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(stringResource(R.string.version), color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text("2.0.0", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text.uppercase(), color = NoorColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun ThemeRow(title: String, subtitle: String, value: String, selected: String, onClick: () -> Unit) {
    val isSelected = value == selected
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        Box(
            modifier = Modifier.size(24.dp).clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) NoorColors.Emerald else MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            if (isSelected) {
                Icon(Icons.Default.Check, "Selected", tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(15.dp))
            }
        }
    }
}

@Composable
private fun LanguageRow(title: String, subtitle: String, value: String, selected: String, onClick: () -> Unit) {
    val isSelected = value == selected
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        Box(
            modifier = Modifier.size(24.dp).clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) NoorColors.Emerald else MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            if (isSelected) {
                Icon(Icons.Default.Check, "Selected", tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(15.dp))
            }
        }
    }
}

@Composable
private fun Divider() {
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 16.dp).background(MaterialTheme.colorScheme.outline))
}
