package com.jonecx.qio.feature.profile

import com.jonecx.qio.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileInfoUseCase @Inject constructor(private val apiService: ApiService) {
    operator fun invoke(): Flow<ProfileInfoState> = apiService.getSelfProfile()
}
