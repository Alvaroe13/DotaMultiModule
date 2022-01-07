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

/**
 * 1. Success (Retrieve a list of heros)
 * 2. Failure (Retrieve an empty list of heros)
 * 3. Failure (Retrieve malformed data (empty cache))
 * 4. Success (Retrieve malformed data but still returns data from cache)
 */
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
    fun `failed retrieving data from server (MALFORMED DATA), successfully get empty list from cache since no new item was added`() =
        runBlocking {

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
            val cachedHeros = cache.selectAll()
            assert(cachedHeros.isEmpty())

            val emissions = getHeros.execute().toList()

            assert(emissions[0] == DataState.Loading<List<Hero>>(ProgressBarState.Loading))

            //Confirm data is malformed
            assert(emissions[1] is DataState.Response<List<Hero>>)
            val response = emissions[1] as DataState.Response
            val dialogResponse = response.uiComponent as UIComponent.Dialog
            assert(dialogResponse.title == "Network Data Error")
            assert(dialogResponse.message.contains("Unexpected JSON token at offset"))

            //confirm cache is STILL empty
            // assert(cache.selectAll().isEmpty())
            assert(emissions[2] is DataState.Data)
            val data = emissions[2] as DataState.Data<List<Hero>>
            assert(data.data?.size == 0)

            //confirm pb state in idle
            assert(emissions[3] == DataState.Loading<List<Hero>>(ProgressBarState.Idle))
        }

    @Test
    fun ` failed retrieving data from server (MALFORMED DATA), therefore no new hero added to cache but retrieved successfully the ones already in cache`() =
        runBlocking {

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
            assert(
                ((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).message.contains(
                    "Unexpected JSON token at offset"
                )
            )

            // Confirm third emission is data from the cache
            assert(emissions[2] is DataState.Data)
            assert((emissions[2] as DataState.Data).data?.size == HeroDataValid.NUM_HEROS)

            // Confirm that no new elements has been added to cache since data from server was malformed and threw an exception
            cachedHeros = cache.selectAll()
            assert(cachedHeros.size == HeroDataValid.NUM_HEROS)

            // Confirm loading state is IDLE
            assert(emissions[3] == DataState.Loading<List<Hero>>(ProgressBarState.Idle))
        }

    @Test
    fun `server returns empty list and nothing breaks`() = runBlocking {

        // setup
        val cache = HeroCacheFake(database = HeroDatabaseFake())
        val service = HeroServiceFake.build(
            type = HeroServiceResponseType.EmptyList // malformed data in json payload
        )

        getHeros = GetHeros(
            cache = cache,
            service = service
        )

        // init value is empty
        val cachedHeroes = cache.selectAll()
        assert(cachedHeroes.isEmpty())

        //execute use-case
        val emissions = getHeros.execute().toList()

        //Confirm pb is loading
        assert(emissions[0] == DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Loading))

        //confirms data from server is empty
        assert(emissions[1] is DataState.Data)
        val data = emissions[1] as DataState.Data<List<Hero>>
        assert(data.data?.size == 0)

        //confirm dara from cache is empty as well
        assert(cache.selectAll().isEmpty())

        //Confirm pb is idle
        assert(emissions[2] == DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Idle))
    }

}