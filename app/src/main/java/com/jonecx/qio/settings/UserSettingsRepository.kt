package com.jonecx.qio.settings

import com.jonecx.qio.settings.proto.ThemeConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserSettingsRepository @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
) : SettingsRepository {

    override val settingsData: Flow<SettingsData> = settingsDataStore.settingsData

    override suspend fun setTheme(themeConfig: ThemeConfig) {
        settingsDataStore.setThemeConfig(themeConfig)
    }

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        settingsDataStore.setUseDynamicColor(useDynamicColor)
    }
}
