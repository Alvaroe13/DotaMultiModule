package com.alvaro.hero_interactors

import com.alvaro.hero_datasource.cache.HeroCache
import com.alvaro.hero_datasource.network.HeroService
import com.squareup.sqldelight.db.SqlDriver

class HeroInteractors private constructor(
    val getHeros: GetHeros,
    var getHeroDetails: GetHeroDetails
) {

    companion object Factory {

        fun build(sqlDriver: SqlDriver): HeroInteractors {
            val service = HeroService.build()
            val cache = HeroCache.build(sqlDriver)
            return HeroInteractors(
                getHeros = GetHeros(
                    service = service,
                    cache = cache
                ),
                getHeroDetails = GetHeroDetails(
                    cache = cache
                )
            )
        }

        val schema: SqlDriver.Schema = HeroCache.schema
        val dbName: String = HeroCache.dbName

    }

}