package com.alvaro.dotamultimodule.di

import android.app.Application
import com.alvaro.hero_interactors.HeroInteractors
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroInteractorsModule {


    @Provides
    @Singleton
    @Named("heroAndroidSqlDriver")
    fun provideAndroidDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = HeroInteractors.schema,
            context = app,
            name = HeroInteractors.dbName,
        )
    }

    @Provides
    @Singleton
    fun provideheoInteractors(
        @Named("heroAndroidSqlDriver") sqlDriver: SqlDriver
    ) : HeroInteractors{
        return HeroInteractors.build(sqlDriver = sqlDriver)
    }
}