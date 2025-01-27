package me.shawaf.themuslimapp.data.local.prefers

import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPreferencesManagerImp(
    private var sharedPreferences: SharedPreferences, private var gson: Gson
) : SharedPreferencesManager {
    override suspend fun saveConfig(configModel: ConfigModel) {
        sharedPreferences.edit().putString(KEY_CONFIG_MODEL_PREFERS, gson.toJson(configModel))
            .apply()
    }

    override suspend fun getConfig(): ConfigModel {
        val savedString = sharedPreferences.getString(KEY_CONFIG_MODEL_PREFERS, null)
        return if (savedString.isNullOrEmpty().not()) {
            gson.fromJson(savedString, ConfigModel::class.java)
        } else {
            ConfigModel(soundEnabled = true, vibrationEnabled = true, selectedThemeIndex = 0)
        }
    }


}