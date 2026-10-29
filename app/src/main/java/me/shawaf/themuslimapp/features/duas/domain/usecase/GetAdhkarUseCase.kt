package me.shawaf.themuslimapp.features.duas.domain.usecase

import me.shawaf.themuslimapp.data.local.model.AdhkarItem
import me.shawaf.themuslimapp.features.counter.domain.usecase.GetJsonZekrListUseCase
import javax.inject.Inject

/**
 * The single source of truth for the merged Azkar content: the duas.json
 * categories plus the after-salah zekr.json, folded together and de-duplicated.
 * Used by both the Azkar grid and the counter so they stay in sync.
 */
class GetAdhkarUseCase @Inject constructor(
    private val getDuaCategoriesUseCase: GetDuaCategoriesUseCase,
    private val getJsonZekrListUseCase: GetJsonZekrListUseCase,
) {
    suspend operator fun invoke(): List<AdhkarItem> {
        val cats = getDuaCategoriesUseCase()
        // duas.json "after_prayer" is the same set as the zekr.json after-salah azkar — fold it in.
        val duaItems = cats.flatMap { c ->
            val merged = c.id == AFTER_PRAYER
            val cid = if (merged) AFTER_SALAH else c.id
            val cAr = if (merged) AFTER_SALAH_AR else c.titleAr
            val cEn = if (merged) AFTER_SALAH_EN else c.titleEn
            c.duas.map { d -> AdhkarItem(d.ar, d.en, d.translit, d.count, d.reference, cid, cAr, cEn) }
        }
        val zekr = runCatching { getJsonZekrListUseCase() }.getOrDefault(emptyList())
        val afterSalah = zekr.map { z ->
            AdhkarItem(
                ar = z.zekr, en = z.zekr_en, translit = "", count = z.count,
                reference = z.sanad, categoryId = AFTER_SALAH,
                categoryAr = AFTER_SALAH_AR, categoryEn = AFTER_SALAH_EN,
            )
        }
        // After-salah first (most counted); drop duplicates present in both sources.
        return (afterSalah + duaItems).distinctBy { it.ar.trim() }
    }

    companion object {
        const val AFTER_SALAH = "after_salah"
        const val AFTER_PRAYER = "after_prayer"
        const val AFTER_SALAH_AR = "أذكار بعد الصلاة"
        const val AFTER_SALAH_EN = "After Salah"
    }
}
