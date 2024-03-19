package com.jonecx.qio.feature.profile.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jonecx.qio.feature.profile.previews.PreviewParameterData.profileInfo
import com.jonecx.qio.model.Meta
import com.jonecx.qio.model.PostFormat
import com.jonecx.qio.model.ProfileInfo
import com.jonecx.qio.model.Response
import com.jonecx.qio.model.User

class ProfilePreviewParameterProvider : PreviewParameterProvider<ProfileInfo> {
    override val values: Sequence<ProfileInfo> = sequenceOf(profileInfo)
}

object PreviewParameterData {
    private val meta = Meta(200, "Ok")
    private val user = User(10, PostFormat.HTML, "bob", 200, emptyList())
    private val response = Response(user)
    val profileInfo = ProfileInfo(meta, response)
}
