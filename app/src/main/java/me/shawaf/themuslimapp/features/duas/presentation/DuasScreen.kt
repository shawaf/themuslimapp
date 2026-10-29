package me.shawaf.themuslimapp.features.duas.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.R
import me.shawaf.themuslimapp.data.local.model.DuaCategory
import me.shawaf.themuslimapp.data.local.model.DuaItem
import me.shawaf.themuslimapp.features.duas.presentation.viewmodel.DuasIntent
import me.shawaf.themuslimapp.features.duas.presentation.viewmodel.DuasViewModel
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors

@Composable
fun DuasScreen(duasViewModel: DuasViewModel) {
    val state by duasViewModel.viewState.collectAsState()
    val selected = state.categories.firstOrNull { it.id == state.selectedCategoryId } ?: state.categories.firstOrNull()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 18.dp)) {
        Text("Duas & Azkar", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
            item { SearchField(state.searchQuery) { duasViewModel.handleIntent(DuasIntent.Search(it)) } }
            item {
                CategoryChips(state.categories, state.selectedCategoryId) {
                    duasViewModel.handleIntent(DuasIntent.SelectCategory(it))
                }
            }
            if (state.categories.size >= 2) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                        FeaturedCard(state.categories[0], state.categories[0].id == state.selectedCategoryId, R.drawable.ic_w_fajr, Modifier.weight(1f)) {
                            duasViewModel.handleIntent(DuasIntent.SelectCategory(state.categories[0].id))
                        }
                        FeaturedCard(state.categories[1], state.categories[1].id == state.selectedCategoryId, R.drawable.ic_w_isha, Modifier.weight(1f)) {
                            duasViewModel.handleIntent(DuasIntent.SelectCategory(state.categories[1].id))
                        }
                    }
                }
            }
            item {
                Text(selected?.titleEn ?: "", color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp,
                    fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            }
            val duas = state.visibleDuas
            if (!state.isLoading && duas.isEmpty()) {
                item { Text("No duas found", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp) }
            } else {
                items(duas) { dua -> DuaCard(dua) }
            }
        }
    }
}

@Composable
private fun FeaturedCard(category: DuaCategory, selected: Boolean, iconRes: Int, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .shadow(if (selected) 8.dp else 4.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (selected) Brush.linearGradient(listOf(NoorColors.Emerald, NoorColors.EmeraldDark))
                else Brush.linearGradient(listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface)),
            )
            .clickable(onClick = onClick).padding(15.dp),
    ) {
        Icon(painterResource(iconRes), null, tint = if (selected) NoorColors.GoldLight else NoorColors.Gold, modifier = Modifier.size(16.dp))
        Spacer(Modifier.height(10.dp))
        Text("${category.titleEn} Azkar", color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text("${category.duas.size} adhkar", color = if (selected) Color(0xFFA9CCC0) else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
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
            if (query.isEmpty()) Text("Search duas & azkar…", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            BasicTextField(
                value = query, onValueChange = onQueryChange, singleLine = true,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp),
                cursorBrush = SolidColor(NoorColors.Gold), modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun CategoryChips(categories: List<DuaCategory>, selectedId: String, onSelect: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        categories.forEach { category ->
            val sel = category.id == selectedId
            Text(
                text = category.titleEn,
                color = if (sel) NoorColors.Surface else NoorColors.Emerald,
                fontSize = 12.sp, fontWeight = if (sel) FontWeight.Bold else FontWeight.SemiBold,
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(if (sel) NoorColors.Emerald else MaterialTheme.colorScheme.surface)
                    .clickable { onSelect(category.id) }.padding(horizontal = 15.dp, vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun DuaCard(dua: DuaItem) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface).padding(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            if (dua.translit.isNotEmpty()) {
                Text(dua.translit, color = NoorColors.Emerald, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(horizontal = 9.dp, vertical = 3.dp))
            } else Spacer(Modifier.size(1.dp))
            if (dua.count > 1) Text("×${dua.count}", color = NoorColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        Text(dua.ar, color = MaterialTheme.colorScheme.onBackground, fontSize = 21.sp, fontFamily = AmiriFontFamily,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.End, lineHeight = 38.sp, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        Text(dua.en, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 18.sp)
        if (dua.reference.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(dua.reference, color = NoorColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
