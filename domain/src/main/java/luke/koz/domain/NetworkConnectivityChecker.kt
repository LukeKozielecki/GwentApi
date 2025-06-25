package luke.koz.domain

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityChecker {
    fun observeInternetAvailability(): Flow<Boolean>
}