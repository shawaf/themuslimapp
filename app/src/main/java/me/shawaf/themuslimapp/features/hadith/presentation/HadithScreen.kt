package me.shawaf.themuslimapp.features.hadith.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            state.collections.forEach { collection ->
                CollectionCard(collection, selected = collection.id == state.selectedCollection?.id) {
                    hadithViewModel.handleIntent(HadithIntent.SelectCollection(collection.id))
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
            hadiths.firstOrNull()?.let { featured -> item { FeaturedHadith(featured) } }
            item {
                Text("Browse hadith", color = MaterialTheme.colorScheme.onBackground, fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp, bottom = 2.dp))
            }
            itemsIndexed(hadiths) { index, hadith -> BrowseRow(index + 1, hadith) }
        }
    }
}

@Composable
private fun CollectionCard(collection: HadithCollection, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(132.dp)
            .shadow(if (selected) 8.dp else 4.dp, RoundedCornerShape(16.dp), clip = false)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected) Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark))
                else Brush.linearGradient(listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface)),
            )
            .clickable(onClick = onClick).padding(14.dp),
    ) {
        Text("Collection", color = if (selected) Color(0xFFA9CCC0) else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text(collection.nameEn, color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("${"%,d".format(collection.total)} hadith", color = if (selected) NoorColors.GoldLight else NoorColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
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
        Text(hadith.ar, color = Color.White, fontSize = 21.sp, fontFamily = AmiriFontFamily, fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End, lineHeight = 36.sp, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        Text(hadith.en, color = Color.White.copy(alpha = 0.8f), fontSize = 12.5.sp, lineHeight = 18.sp)
        Spacer(Modifier.height(8.dp))
        Text("${hadith.narrator} · ${hadith.reference}", color = NoorColors.GoldLight, fontSize = 11.sp)
    }
}

@Composable
private fun BrowseRow(number: Int, hadith: HadithItem) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(15.dp), clip = false)
            .clip(RoundedCornerShape(15.dp)).background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(34.dp).clip(RoundedCornerShape(11.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Text("%02d".format(number), color = NoorColors.Emerald, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(hadith.en, color = MaterialTheme.colorScheme.onBackground, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("${hadith.narrator} · ${hadith.reference}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
        Spacer(Modifier.size(8.dp))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
    }
}
