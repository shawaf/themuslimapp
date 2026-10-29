package me.shawaf.themuslimapp.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Per-app language handling. The app defaults to Arabic (RTL); English is
 * switchable from Settings. The choice is stored in a small dedicated prefs
 * file so it can be read synchronously from Activity.attachBaseContext.
 */
object LocaleHelper {

    const val ARABIC = "ar"
    const val ENGLISH = "en"
    private const val PREFS = "app_locale_prefs"
    private const val KEY_LANGUAGE = "app_language"

    /** True when the current app locale is Arabic. */
    fun isArabic(): Boolean = Locale.getDefault().language == ARABIC

    fun savedLanguage(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, ARABIC) ?: ARABIC

    fun saveLanguage(context: Context, language: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY_LANGUAGE, language).apply()
    }

    /** Wraps the context with the given language + matching layout direction. */
    fun wrap(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}
