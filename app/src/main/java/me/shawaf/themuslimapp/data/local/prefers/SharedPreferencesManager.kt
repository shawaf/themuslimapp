package me.shawaf.themuslimapp.data.local.prefers

import me.shawaf.themuslimapp.data.local.ConfigModel

interface SharedPreferencesManager {
    suspend fun saveConfig(configModel: ConfigModel)
    suspend fun getConfig() : ConfigModel
}