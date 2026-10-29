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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.data.local.model.DuaCategory
import me.shawaf.themuslimapp.data.local.model.DuaItem
import me.shawaf.themuslimapp.features.duas.presentation.viewmodel.DuasIntent
import me.shawaf.themuslimapp.features.duas.presentation.viewmodel.DuasViewModel
import me.shawaf.themuslimapp.ui.theme.AmiriFontFamily
import me.shawaf.themuslimapp.ui.theme.NoorColors

@Composable
fun DuasScreen(duasViewModel: DuasViewModel) {
    val state by duasViewModel.viewState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 18.dp),
    ) {
        Text("Duas & Azkar", color = NoorColors.Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))
        SearchField(state.searchQuery) { duasViewModel.handleIntent(DuasIntent.Search(it)) }
        Spacer(Modifier.height(14.dp))
        CategoryChips(state.categories, state.selectedCategoryId) {
            duasViewModel.handleIntent(DuasIntent.SelectCategory(it))
        }
        Spacer(Modifier.height(14.dp))

        val duas = state.visibleDuas
        if (!state.isLoading && duas.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No duas found", color = NoorColors.Muted, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                items(duas) { dua -> DuaCard(dua) }
            }
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(14.dp), clip = false)
            .clip(RoundedCornerShape(14.dp))
            .background(NoorColors.Surface)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.Search, null, tint = NoorColors.Muted, modifier = Modifier.size(18.dp))
        Spacer(Modifier.size(10.dp))
        Box(Modifier.fillMaxWidth()) {
            if (query.isEmpty()) {
                Text("Search duas & azkar…", color = NoorColors.Muted, fontSize = 14.sp)
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(color = NoorColors.Ink, fontSize = 14.sp),
                cursorBrush = SolidColor(NoorColors.Gold),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun CategoryChips(categories: List<DuaCategory>, selectedId: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
    ) {
        categories.forEach { category ->
            val selected = category.id == selectedId
            Text(
                text = category.titleEn,
                color = if (selected) NoorColors.Surface else NoorColors.Emerald,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (selected) NoorColors.Emerald else NoorColors.Surface)
                    .clickable { onSelect(category.id) }
                    .padding(horizontal = 15.dp, vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun DuaCard(dua: DuaItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp))
            .background(NoorColors.Surface)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            if (dua.translit.isNotEmpty()) {
                Text(
                    dua.translit,
                    color = NoorColors.Emerald,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(NoorColors.SurfaceTint)
                        .padding(horizontal = 9.dp, vertical = 3.dp),
                )
            } else {
                Spacer(Modifier.size(1.dp))
            }
            if (dua.count > 1) {
                Text("×${dua.count}", color = NoorColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            dua.ar,
            color = NoorColors.Ink,
            fontSize = 21.sp,
            fontFamily = AmiriFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            lineHeight = 38.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(10.dp))
        Text(dua.en, color = NoorColors.Muted, fontSize = 12.sp, lineHeight = 18.sp)
        if (dua.reference.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(dua.reference, color = NoorColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
