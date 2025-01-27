package me.shawaf.themuslimapp.data.local.prefers

interface SharedPreferencesManager {
    suspend fun saveConfig(configModel: ConfigModel)
    suspend fun getConfig() : ConfigModel
}