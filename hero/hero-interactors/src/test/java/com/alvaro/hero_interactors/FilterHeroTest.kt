package com.alvaro.hero_interactors

import com.alvaro.core.domain.FilterOrder
import com.alvaro.hero_datasource_test.network.data.HeroDataValid
import com.alvaro.hero_datasource_test.network.serializeHeroData
import com.alvaro.hero_domain.HeroAttribute
import com.alvaro.hero_domain.HeroFilter
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.math.round
import kotlin.random.Random

/**
 * 1. Success (retrieved hero searched by name)
 * 2. Success (Order by localizedName name DESC)
 * 3. Success (Order by localizedName name ASC)
 * 4. Success (Order by 'proWins' % ASC)
 * 5. Success (Order by 'proWins' % DESC)
 * 6. Success (Filter by 'HeroAttribute' "Strength")
 * 7. Success (Filter by 'HeroAttribute' "Agility")
 * 8. Success (Filter by 'HeroAttribute' "Intelligence")
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
           // println("testTAG, name query ${nameQuery}, hero filtered name= ${heroFiltered.localizedName}")
            if (heroFiltered.localizedName == nameQuery) {
                heroFound = true
            }
        }
        assert(heroFound)
    }

    @Test
    fun `order hero list by localized name DESCENDING order success`() = runBlocking {

        val heroList = serializeHeroData(jsonData = HeroDataValid.data)

        filterHeros = FilterHeros()

        //execute use-case
        val heroListFiltered = filterHeros.execute(
            currentList = heroList,
            heroNameQuery = "",
            heroFilter = HeroFilter.Hero(order = FilterOrder.Descending),
            attributeFilter = HeroAttribute.Unknown
        )

        // confirm they are ordered Z-A
        for (index in 1 until heroListFiltered.size - 1) {
            val heroCurrent = heroListFiltered[index]
            val heroNext = heroListFiltered[index + 1]
            // println("HeroTestTAG, current= ${heroCurrent.localizedName}, next= ${heroNext.localizedName}")
            assert(heroCurrent.localizedName.toCharArray()[0] >= heroNext.localizedName.toCharArray()[0])
        }
    }

    @Test
    fun `order hero list by localized name ASCENDING order success`() = runBlocking {

        val heroList = serializeHeroData(jsonData = HeroDataValid.data)

        filterHeros = FilterHeros()

        //execute use-case
        val heroListFiltered = filterHeros.execute(
            currentList = heroList,
            heroNameQuery = "",
            heroFilter = HeroFilter.Hero(order = FilterOrder.Ascending),
            attributeFilter = HeroAttribute.Unknown
        )

        // confirm they are ordered A-Z
        for (index in 1 until heroListFiltered.size - 1) {
            val heroCurrent = heroListFiltered[index]
            val heroNext = heroListFiltered[index + 1]
           // println("HeroTestTAG, current= ${heroCurrent.localizedName}, next= ${heroNext.localizedName}")
            assert(heroCurrent.localizedName.toCharArray()[0] <= heroNext.localizedName.toCharArray()[0])
        }
    }

    @Test
    fun `order hero list by proWins name ASCENDING order success`() = runBlocking {

        val heroList = serializeHeroData(jsonData = HeroDataValid.data)

        filterHeros = FilterHeros()

        //execute use-case
        val heroListFiltered = filterHeros.execute(
            currentList = heroList,
            heroNameQuery = "",
            heroFilter = HeroFilter.ProWins(order = FilterOrder.Ascending),
            attributeFilter = HeroAttribute.Unknown
        )

        // confirm they are ordered ASCENDING order
        for (index in 1 until heroListFiltered.size - 1) {
            val heroCurrent = heroListFiltered[index]
            val heroNext = heroListFiltered[index + 1]

            val heroCurrentProWinRate =
                round(heroCurrent.proWins.toDouble() / heroCurrent.proPick.toDouble() * 100).toInt()
            val heroNextProWinRate =
                round(heroNext.proWins.toDouble() / heroNext.proPick.toDouble() * 100).toInt()

            //println("HeroTestTAG, current= ${heroCurrent.localizedName}, ${heroCurrentProWinRate}, next= ${heroNext.localizedName}, ${heroNextProWinRate}")
            assert(heroCurrentProWinRate <= heroNextProWinRate)
        }
    }


    @Test
    fun `order hero list by proWins name DESCENDING order success`() = runBlocking {

        val heroList = serializeHeroData(jsonData = HeroDataValid.data)

        filterHeros = FilterHeros()

        //execute use-case
        val heroListFiltered = filterHeros.execute(
            currentList = heroList,
            heroNameQuery = "",
            heroFilter = HeroFilter.ProWins(order = FilterOrder.Descending),
            attributeFilter = HeroAttribute.Unknown
        )

        // confirm they are ordered DESCENDING  order
        for (index in 1 until heroListFiltered.size - 1) {
            val heroCurrent = heroListFiltered[index]
            val heroNext = heroListFiltered[index + 1]

            val heroCurrentProWinRate =
                round(heroCurrent.proWins.toDouble() / heroCurrent.proPick.toDouble() * 100).toInt()
            val heroNextProWinRate =
                round(heroNext.proWins.toDouble() / heroNext.proPick.toDouble() * 100).toInt()

            //println("HeroTestTAG, current= ${heroCurrent.localizedName}, ${heroCurrentProWinRate}, next= ${heroNext.localizedName}, ${heroNextProWinRate}")
            assert(heroCurrentProWinRate >= heroNextProWinRate)
        }
    }

    @Test
    fun`filter heroes by attribute STRENGHT success`() = runBlocking{

        val heroList = serializeHeroData(jsonData = HeroDataValid.data)

        filterHeros = FilterHeros()

        //execute use-case
        val heroListFiltered = filterHeros.execute(
            currentList = heroList,
            heroNameQuery = "",
            heroFilter = HeroFilter.Hero(),
            attributeFilter = HeroAttribute.Strength
        )

        heroListFiltered.forEach{ hero ->
           // println("FilterListTestTAG name = ${hero.localizedName}, attr= ${hero.primaryAttribute.uiValue}")
            assert(hero.primaryAttribute is HeroAttribute.Strength)
        }
    }

    @Test
    fun`filter heroes by attribute AGILITY success`() = runBlocking{

        val heroList = serializeHeroData(jsonData = HeroDataValid.data)

        filterHeros = FilterHeros()

        //execute use-case
        val heroListFiltered = filterHeros.execute(
            currentList = heroList,
            heroNameQuery = "",
            heroFilter = HeroFilter.Hero(),
            attributeFilter = HeroAttribute.Agility
        )

        heroListFiltered.forEach{ hero ->
            //println("FilterListTestTAG name = ${hero.localizedName}, attr= ${hero.primaryAttribute.uiValue}")
            assert(hero.primaryAttribute is HeroAttribute.Agility)
        }
    }

    @Test
    fun`filter heroes by attribute INTELLIGENCE success`() = runBlocking{

        val heroList = serializeHeroData(jsonData = HeroDataValid.data)

        filterHeros = FilterHeros()

        //execute use-case
        val heroListFiltered = filterHeros.execute(
            currentList = heroList,
            heroNameQuery = "",
            heroFilter = HeroFilter.Hero(),
            attributeFilter = HeroAttribute.Intelligence
        )

        heroListFiltered.forEach{ hero ->
            //println("FilterListTestTAG name = ${hero.localizedName}, attr= ${hero.primaryAttribute.uiValue}")
            assert(hero.primaryAttribute is HeroAttribute.Intelligence)
        }
    }

}