package com.jonecx.qio.feature.authentication

import com.jonecx.qio.network.ApiResult
import com.jonecx.qio.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthorizeUseCase @Inject constructor(private val apiService: ApiService) {
    operator fun invoke(authorizationCode: String): Flow<ApiResult<Boolean>> = apiService.authorize(authorizationCode)
}
