package com.jonecx.qio.settings

import androidx.datastore.core.DataStore
import com.jonecx.qio.settings.proto.ThemeConfig
import com.jonecx.qio.settings.proto.UserSettings
import com.jonecx.qio.settings.proto.copy
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsDataStore @Inject constructor(
    private val userSettings: DataStore<UserSettings>,
) {
    val settingsData = userSettings.data
        .map {
            SettingsData(
                it.themConfig,
                it.isUseDynamicColor,
            )
        }

    suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        userSettings.updateData {
            it.copy {
                this.themConfig = themeConfig
            }
        }
    }

    suspend fun setUseDynamicColor(isUseDynamicColor: Boolean) {
        userSettings.updateData {
            it.copy {
                this.isUseDynamicColor = isUseDynamicColor
            }
        }
    }
}
