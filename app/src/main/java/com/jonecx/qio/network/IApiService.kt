package com.jonecx.qio.network

import com.jonecx.qio.feature.profile.ProfileInfoState
import kotlinx.coroutines.flow.Flow

interface

IApiService {

    // https://api.tumblr.com/v2/oauth2/token
    fun authorize(authorizationCode: String): Flow<ApiResult<Boolean>>

    fun getSelfProfile(): Flow<ProfileInfoState>
}
