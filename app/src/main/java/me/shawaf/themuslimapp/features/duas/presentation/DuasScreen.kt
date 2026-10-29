package me.shawaf.themuslimapp.features.duas.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.data.local.model.AdhkarCategory
import me.shawaf.themuslimapp.data.local.model.AdhkarItem
import me.shawaf.themuslimapp.features.duas.presentation.viewmodel.DuasIntent
import me.shawaf.themuslimapp.features.duas.presentation.viewmodel.DuasViewModel
import me.shawaf.themuslimapp.features.duas.presentation.viewmodel.DuasViewState
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors
import me.shawaf.themuslimapp.utils.LocaleHelper

@Composable
fun DuasScreen(
    duasViewModel: DuasViewModel,
    onOpenCategory: (String) -> Unit,
    onOpenCounter: (AdhkarItem) -> Unit,
) {
    val state by duasViewModel.viewState.collectAsState()
    val ar = LocaleHelper.isArabic()
    val searching = state.searchQuery.isNotBlank()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 18.dp)) {
        Text(stringResource(R.string.azkar_title), color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))

        SearchField(state.searchQuery) { duasViewModel.handleIntent(DuasIntent.Search(it)) }
        Spacer(Modifier.height(14.dp))

        if (searching) {
            // Search results: matching adhkar across all categories → straight to the counter.
            val results = state.visible
            if (results.isEmpty()) {
                Text(stringResource(R.string.no_adhkar), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(results) { item -> AdhkarResultCard(item, ar) { onOpenCounter(item) } }
                }
            }
        } else {
            // Category cards: After Salah, Morning, Evening, …
            val categories = state.categories.filter { it.id != DuasViewState.ALL }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(categories) { cat ->
                    val count = state.adhkar.count { it.categoryId == cat.id }
                    CategoryCard(cat, count, ar) { onOpenCategory(cat.id) }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: AdhkarCategory, itemCount: Int, ar: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 118.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp), clip = false)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark)))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(R.drawable.ic_feat_sebha), null, tint = NoorColors.GoldLight, modifier = Modifier.size(22.dp))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = NoorColors.GoldLight.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.height(14.dp))
        Column {
            Text(
                text = if (ar) category.titleAr else category.titleEn,
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 16.sp, fontWeight = FontWeight.Bold,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.adhkar_count, itemCount),
                color = NoorColors.GoldLight, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun AdhkarResultCard(item: AdhkarItem, ar: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp)
            .shadow(5.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(14.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (ar) item.categoryAr else item.categoryEn,
                color = NoorColors.Gold, fontSize = 10.5.sp, fontWeight = FontWeight.Bold,
                maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false),
            )
            Text(
                text = stringResource(R.string.times_count, item.count),
                color = NoorColors.Emerald, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant).padding(horizontal = 8.dp, vertical = 3.dp),
            )
        }
        Spacer(Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            Text(
                text = item.ar,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = AmiriFontFamily, fontWeight = FontWeight.Bold,
                fontSize = 17.sp, lineHeight = 28.sp, textAlign = TextAlign.Center,
                maxLines = 4, overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(14.dp), clip = false)
            .clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
        Spacer(Modifier.size(10.dp))
        Box(Modifier.fillMaxWidth()) {
            if (query.isEmpty()) Text(stringResource(R.string.search_duas), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            BasicTextField(
                value = query, onValueChange = onQueryChange, singleLine = true,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp),
                cursorBrush = SolidColor(NoorColors.Gold), modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
