package com.alvaro.hero_interactors

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.core.domain.UIComponent
import com.alvaro.hero_datasource_test.cache.HeroCacheFake
import com.alvaro.hero_datasource_test.cache.HeroDatabaseFake
import com.alvaro.hero_datasource_test.network.HeroServiceFake
import com.alvaro.hero_datasource_test.network.HeroServiceResponseType
import com.alvaro.hero_datasource_test.network.data.HeroDataValid
import com.alvaro.hero_datasource_test.network.serializeHeroData
import com.alvaro.hero_domain.Hero
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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

        //confirms cache is empty first
        var cachedHeros = cache.selectAll()
        assert(cachedHeros.isEmpty())

        // execute use-case
        val emissions = getHeros.execute().toList()

        //--------- from here on we will test all possible emissions sent by use case  -----//

        // confirms emission at this point is = show loading progressBar
        assert(emissions[0] == DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Loading))

        // confirms emission at this point is = data
        assert(emissions[1] is DataState.Data)
        assert((emissions[1] as DataState.Data).data?.size ?: 0 == HeroDataValid.NUM_HEROS)

        //confirms emission at this point is = cache is not empty anymore
        cachedHeros = cache.selectAll()
        assert(cachedHeros.size == HeroDataValid.NUM_HEROS)

        //confirms emission at this point is = progressBar is in idle state
        assert(emissions[2] == DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Idle))
    }

    @Test
    fun `get heroes MALFORMED DATA  success from cache`() = runBlocking {

        // setup
        val cache = HeroCacheFake(database = HeroDatabaseFake())
        val service = HeroServiceFake.build(
            type = HeroServiceResponseType.MalformedData // malformed data in json payload
        )

        getHeros = GetHeros(
            cache = cache,
            service = service
        )

        //confirms cache is empty first
        var cachedHeros = cache.selectAll()
        assert(cachedHeros.isEmpty())


        // Add some data to the cache by executing a successful request
        val heroData = serializeHeroData(jsonData = HeroDataValid.data)
        cache.insert(heros = heroData)

        // Confirm the cache is not empty anymore
        cachedHeros = cache.selectAll()
        assert(cachedHeros.size == HeroDataValid.NUM_HEROS)

        // Execute the use-case
        val emissions = getHeros.execute().toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading<List<Hero>>(ProgressBarState.Loading))

        // Confirm second emission is error response
        assert(emissions[1] is DataState.Response)
        assert(((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).title == "Network Data Error")
        assert(((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).message.contains("Unexpected JSON token at offset"))

        // Confirm third emission is data from the cache
        assert(emissions[2] is DataState.Data)
        assert((emissions[2] as DataState.Data).data?.size == HeroDataValid.NUM_HEROS)

        // Confirm the cache is still not empty
        cachedHeros = cache.selectAll()
        assert(cachedHeros.size == HeroDataValid.NUM_HEROS)

        // Confirm loading state is IDLE
        assert(emissions[3] == DataState.Loading<List<Hero>>(ProgressBarState.Idle))
    }

}