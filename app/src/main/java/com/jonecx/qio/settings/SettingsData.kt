package com.jonecx.qio.settings

import com.jonecx.qio.settings.proto.ThemeConfig

data class SettingsData(
    val darkThemeConfig: ThemeConfig,
    val useDynamicColor: Boolean,
    val isShowNavLabel: Boolean,
)
