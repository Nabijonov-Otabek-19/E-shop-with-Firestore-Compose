package uz.gita.androidexam.navigation

interface AppNavigator {
    suspend fun replace(screen: AppScreen)
    suspend fun navigateTo(screen: AppScreen)
    suspend fun back()
}