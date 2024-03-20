package com.jonecx.qio.network

import com.jonecx.qio.feature.profile.ProfileInfoState
import com.jonecx.qio.model.OauthTokenInfo
import kotlinx.coroutines.flow.Flow

interface

IApiService {

    // https://api.tumblr.com/v2/oauth2/token
    fun authorize(authorizationCode: String): Flow<ApiResult<OauthTokenInfo>>

    fun refreshToken(oauthTokenInfo: OauthTokenInfo): Flow<ApiResult<OauthTokenInfo>>
    fun getSelfProfile(): Flow<ProfileInfoState>
}
