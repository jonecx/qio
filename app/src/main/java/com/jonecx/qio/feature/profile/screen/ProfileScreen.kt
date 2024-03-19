package com.jonecx.qio.feature.profile.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonecx.qio.R
import com.jonecx.qio.feature.profile.ProfileInfoState
import com.jonecx.qio.feature.profile.previews.ProfilePreviewParameterProvider
import com.jonecx.qio.model.ProfileInfo
import com.jonecx.qio.ui.theme.QioBackground
import com.jonecx.qio.ui.theme.QioTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import previews.DevicePreviewProfiles

@Composable
internal fun ProfileScreen(profileInfoState: ProfileInfoState, onEdit: () -> Unit, onShare: () -> Unit, onSettings: () -> Unit) {
    val state = rememberCollapsingToolbarScaffoldState()
    val profileInfo = when (profileInfoState) {
        is ProfileInfoState.Success -> profileInfoState.profileInfo.getProfile()
        else -> null
    }
    CollapsingToolbarScaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
                    .fillMaxWidth()
                    .height(250.dp)
                    .pin(),
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .road(Alignment.TopStart, Alignment.TopEnd),
                horizontalAlignment = Alignment.End,
            ) {
                Row(horizontalArrangement = Arrangement.End) {
                    ProfileButton(modifier = Modifier.padding(end = 16.dp), imageVector = Icons.Filled.Edit, contentDescription = R.string.edit_profile, onEdit)
                    ProfileButton(modifier = Modifier.padding(end = 16.dp), imageVector = Icons.Filled.Share, contentDescription = R.string.share_profile, onShare)
                    ProfileButton(imageVector = Icons.Filled.Settings, contentDescription = R.string.settings, onClickEvent = onSettings)
                }
            }

            Image(
                modifier = Modifier
                    .road(Alignment.TopCenter, Alignment.Center)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary),
                imageVector = Icons.Filled.AccountBox,
                contentDescription = null,
            )

            Text(
                text = profileInfo?.name ?: "",
                modifier = Modifier
                    .road(Alignment.CenterStart, Alignment.BottomCenter)
                    .padding(16.dp),
                color = Color.White,
                fontSize = textSize,
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            items(100) {
                Text(
                    text = "Item $it",
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@Composable
fun ProfileButton(modifier: Modifier = Modifier, imageVector: ImageVector, contentDescription: Int, onClickEvent: () -> Unit) {
    Image(imageVector = imageVector, contentDescription = stringResource(id = contentDescription), modifier = modifier.clickable { onClickEvent() })
}

@DevicePreviewProfiles
@Composable
fun ProfileScreenPreview(
    @PreviewParameter(ProfilePreviewParameterProvider::class)
    profileInfo: ProfileInfo,
) {
    QioTheme {
        QioBackground {
            ProfileScreen(
                profileInfoState = ProfileInfoState.Success(profileInfo),
                onEdit = {},
                onShare = {},
                onSettings = {},
            )
        }
    }
}
