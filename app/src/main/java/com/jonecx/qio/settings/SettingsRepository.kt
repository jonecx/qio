package com.jonecx.qio.settings

import com.jonecx.qio.settings.proto.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsData: Flow<SettingsData>
    suspend fun setTheme(themeConfig: ThemeConfig)
    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
}
