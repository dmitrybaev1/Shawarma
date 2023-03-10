package ru.shawarma.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.shawarma.core.data.services.AuthService
import ru.shawarma.core.data.services.MenuService
import ru.shawarma.core.data.utils.AppRetrofit

@InstallIn(SingletonComponent::class)
@Module
object DataModuleProvider {

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideAuthService(): AuthService = AppRetrofit.authService

    @Provides
    fun provideMenuService(): MenuService = AppRetrofit.menuService
}