package com.alvaro.ui_herodetail.di

import com.alvaro.core.util.Logger
import com.alvaro.hero_interactors.GetHeroDetails
import com.alvaro.hero_interactors.HeroInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroDetailsModule {


    @Provides
    @Singleton
    @Named("heroDetailsLogger")
    fun provideLogger(): Logger {
        return Logger(
            tag = "HerodetailView",
            isDebug = true
        )
    }


    @Provides
    @Singleton
    fun provideGetHeroDetailsInteractor(
        interactors: HeroInteractors
    ): GetHeroDetails{
        return interactors.getHeroDetails
    }


}