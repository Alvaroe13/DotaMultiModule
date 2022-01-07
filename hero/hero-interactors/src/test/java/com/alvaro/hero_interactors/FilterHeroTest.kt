package com.alvaro.hero_interactors

import com.alvaro.hero_datasource_test.network.data.HeroDataValid
import com.alvaro.hero_datasource_test.network.serializeHeroData
import com.alvaro.hero_domain.HeroAttribute
import com.alvaro.hero_domain.HeroFilter
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.random.Random

/**
 * 1. retrieved hero searched by name
 */
class FilterHeroTest {

    //system in test
    private lateinit var filterHeros: FilterHeros

    @Test
    fun `retrieved correct hero from cache when searching by name`() = runBlocking {

        val heroList = serializeHeroData(jsonData = HeroDataValid.data)

        //choose random hero from heroList
        val hero = heroList.get(Random.nextInt(from = 0, until = heroList.size - 1))
        val nameQuery: String = hero.localizedName
        filterHeros = FilterHeros()

        //execute use-case
        val heroListFilter = filterHeros.execute(
            currentList = heroList,
            heroNameQuery = nameQuery,
            heroFilter = HeroFilter.Hero(),
            attributeFilter = HeroAttribute.Unknown
        )

        //confirm list filtered is not empty
        assert(heroListFilter.isNotEmpty())

        //confirm name inserted as query is contain in list filtered
        var heroFound = false
        heroListFilter.forEach { heroFiltered ->
            println("testTAG, name query ${nameQuery}, hero filtered name= ${heroFiltered.localizedName}")
            if (heroFiltered.localizedName == nameQuery) {
                heroFound = true
            }
        }
        assert(heroFound)
    }

}