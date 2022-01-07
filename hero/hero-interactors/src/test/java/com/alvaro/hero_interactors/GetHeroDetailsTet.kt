package com.alvaro.hero_interactors

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.core.domain.UIComponent
import com.alvaro.hero_datasource_test.cache.HeroCacheFake
import com.alvaro.hero_datasource_test.cache.HeroDatabaseFake
import com.alvaro.hero_datasource_test.network.data.HeroDataValid
import com.alvaro.hero_datasource_test.network.serializeHeroData
import com.alvaro.hero_domain.Hero
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.random.Random

/**
 * 1. Successfully retrieved hero's details from cache
 * 2. Failed retrieving hero's detail from cache
 */
class GetHeroDetailsTet {

    //system in test
    private lateinit var getHeroDetails: GetHeroDetails


    @Test
    fun `retrieved hero's details from cache success`() = runBlocking {

        val heroCache = HeroCacheFake(database = HeroDatabaseFake())

        getHeroDetails = GetHeroDetails(cache = heroCache)

        //insert hero in cache
        val heroList = serializeHeroData(HeroDataValid.data)
        heroCache.insert(heroList)

        // choose random id hero from data
        val randomId = Random.nextInt(from = 0, until = heroList.size - 1)
        val hero = heroList.get(randomId)

        //execute use-case
        val emissions = getHeroDetails.execute(heroId = hero.id).toList()

        //Confirm pb is Loading
        assert(emissions[0] == DataState.Loading<Hero>(progressBarState = ProgressBarState.Loading))

        //confirms data retrieved is right one
        assert(emissions[1] is DataState.Data<Hero>)
        val data = (emissions[1] as DataState.Data<Hero>).data
        assert(data?.id == hero.id)
        assert(data?.localizedName == hero.localizedName)

        //Confirm pb is Idle
        assert(emissions[2] == DataState.Loading<Hero>(progressBarState = ProgressBarState.Idle))
    }

    @Test
    fun `error retrieving hero's details from cache`() = runBlocking{

        val heroCache = HeroCacheFake(database = HeroDatabaseFake())

        getHeroDetails = GetHeroDetails(cache = heroCache)

        //insert hero in cache
        val heroList = serializeHeroData(HeroDataValid.data)
        // choose random id hero from data
        val randomId = Random.nextInt(from = 0, until = heroList.size - 1)
        val hero = heroList.get(randomId)

        //Confirm cache is empty
        assert(heroCache.selectAll().isEmpty())

        //execute use-case
        val emissions = getHeroDetails.execute(heroId = hero.id).toList()

        //Confirm pb is Loading
        assert(emissions[0] == DataState.Loading<Hero>(progressBarState = ProgressBarState.Loading))

        //confirm error received from cache since hero was not found
        assert(emissions[1] is DataState.Response<Hero>)
        val uiComponent = (emissions[1] as DataState.Response<Hero>).uiComponent
        val dialog = uiComponent as UIComponent.Dialog
        val title = dialog.title
        val message = dialog.message

        assert(title == "Error")
        assert(message == "That hero does not exist in the cache.")

        //Confirm pb is Idle
        assert(emissions[2] == DataState.Loading<Hero>(progressBarState = ProgressBarState.Idle))

    }

}