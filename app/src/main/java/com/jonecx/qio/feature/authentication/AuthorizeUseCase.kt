package com.jonecx.qio.network

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthorizeUseCase @Inject constructor(private val apiService: ApiService) {
    operator fun invoke(authorizationCode: String): Flow<ApiResult<Boolean>> = apiService.authorize(authorizationCode)
}
