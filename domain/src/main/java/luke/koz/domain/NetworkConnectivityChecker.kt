package luke.koz.domain

interface NetworkConnectivityChecker {
    fun isInternetAvailable(): Boolean
}