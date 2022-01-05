package com.alvaro.hero_interactors

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.hero_datasource_test.cache.HeroCacheFake
import com.alvaro.hero_datasource_test.cache.HeroDatabaseFake
import com.alvaro.hero_datasource_test.network.HeroServiceFake
import com.alvaro.hero_datasource_test.network.HeroServiceResponseType
import com.alvaro.hero_datasource_test.network.data.HeroDataValid.NUM_HEROS
import com.alvaro.hero_domain.Hero
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetHerosTest {

    //system in test
    private lateinit var getHeros: GetHeros


    @Test
    fun getHeros_success() = runBlocking {

        // setup
        val cache = HeroCacheFake(database = HeroDatabaseFake())
        val service = HeroServiceFake.build(
            type = HeroServiceResponseType.GoodData // good data
        )

        getHeros = GetHeros(
            cache = cache,
            service = service
        )

        //let's test that list is empty at first
        var cachedHeros = cache.selectAll()
        assert(cachedHeros.isEmpty())

        // execute use-case
        val emissions = getHeros.execute().toList()

        //--------- from here on we will test all possible emissions sent by use case  -----//

        // show loading progressBar
        assert(emissions[0] == DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Loading))

        //getting data from user-case
        assert(emissions[1] is DataState.Data)
        assert((emissions[1] as DataState.Data).data?.size ?: 0 == NUM_HEROS)

        //CACHE is not empty anymore
        cachedHeros = cache.selectAll()
        assert(cachedHeros.size == NUM_HEROS)

        //progressBar is in idle state
        assert(emissions[2] == DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Idle))
    }

}