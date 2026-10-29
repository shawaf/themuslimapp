package me.shawaf.themuslimapp.features.hadith.presentation
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.data.local.model.HadithCollection
import me.shawaf.themuslimapp.data.local.model.HadithItem
import me.shawaf.themuslimapp.features.hadith.presentation.viewmodel.HadithIntent
import me.shawaf.themuslimapp.features.hadith.presentation.viewmodel.HadithViewModel
import me.shawaf.themuslimapp.ui.components.NoorScreenHeader
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors

@Composable
fun HadithScreen(hadithViewModel: HadithViewModel, onBack: () -> Unit) {
    val state by hadithViewModel.viewState.collectAsState()
    val hadiths = state.hadiths

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 18.dp)) {
        NoorScreenHeader("Hadith Library", onBack)
        Spacer(Modifier.height(14.dp))

        CollectionChips(state.collections, state.selectedCollection?.id.orEmpty()) {
            hadithViewModel.handleIntent(HadithIntent.SelectCollection(it))
        }
        Spacer(Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            hadiths.firstOrNull()?.let { featured ->
                item { FeaturedHadith(featured) }
            }
            if (hadiths.size > 1) {
                items(hadiths.drop(1)) { hadith -> HadithCard(hadith) }
            }
        }
    }
}

@Composable
private fun CollectionChips(collections: List<HadithCollection>, selectedId: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
    ) {
        collections.forEach { collection ->
            val selected = collection.id == selectedId
            Text(
                text = collection.nameEn,
                color = if (selected) MaterialTheme.colorScheme.surface else NoorColors.Emerald,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(if (selected) NoorColors.Emerald else MaterialTheme.colorScheme.surface)
                    .clickable { onSelect(collection.id) }
                    .padding(horizontal = 15.dp, vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun FeaturedHadith(hadith: HadithItem) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(listOf(NoorColors.EmeraldDark, NoorColors.Emerald)))
            .padding(18.dp),
    ) {
        Text("HADITH OF THE DAY", color = NoorColors.GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Text(hadith.ar, color = Color.White, fontSize = 21.sp, fontFamily = AmiriFontFamily,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.End, lineHeight = 36.sp,
            modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        Text(hadith.en, color = Color.White.copy(alpha = 0.8f), fontSize = 12.5.sp, lineHeight = 18.sp)
        Spacer(Modifier.height(8.dp))
        Text("${hadith.narrator} · ${hadith.reference}", color = NoorColors.GoldLight, fontSize = 11.sp)
    }
}

@Composable
private fun HadithCard(hadith: HadithItem) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface).padding(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(hadith.narrator, color = NoorColors.Emerald, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 9.dp, vertical = 3.dp))
            if (hadith.grade.isNotEmpty()) {
                Text(hadith.grade, color = NoorColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(hadith.ar, color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp, fontFamily = AmiriFontFamily,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.End, lineHeight = 36.sp,
            modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        Text(hadith.en, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 18.sp)
        Spacer(Modifier.height(8.dp))
        Text(hadith.reference, color = NoorColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}
