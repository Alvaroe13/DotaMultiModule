package com.alvaro.ui_herolist.di

import com.alvaro.core.util.Logger
import com.alvaro.hero_interactors.GetHeros
import com.alvaro.hero_interactors.HeroInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroListModule {

    @Provides
    @Singleton
    @Named("heroListLogger")
    fun provideLogger(): Logger {
        return Logger(
            tag = "HerolistView",
            isDebug = true
        )
    }


    /**
     * @param interactors is provided in app module.
     */
    @Provides
    @Singleton
    fun provideGetHerosInteractor(
        interactors: HeroInteractors
    ) : GetHeros {
        return interactors.getHeros
    }
}