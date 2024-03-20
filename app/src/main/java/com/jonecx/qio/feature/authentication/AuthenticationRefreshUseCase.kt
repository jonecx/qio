package com.jonecx.qio.feature.authentication

import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.network.ApiResult
import com.jonecx.qio.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthenticationRefreshUseCase @Inject constructor(private val apiService: ApiService) {
    operator fun invoke(oauthTokenInfo: OauthTokenInfo): Flow<ApiResult<OauthTokenInfo>> = apiService.refreshToken(oauthTokenInfo)
}
