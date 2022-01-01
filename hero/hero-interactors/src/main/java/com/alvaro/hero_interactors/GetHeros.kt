package com.alvaro.hero_interactors

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.util.Logger
import com.alvaro.hero_datasource.cache.HeroCache
import com.alvaro.hero_datasource.network.HeroService
import com.alvaro.hero_domain.Hero
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHeros(
    private val service : HeroService,
    private val cache : HeroCache,
    private val logger : Logger? = null
) {

    fun execute() : Flow<DataState<List<Hero>>> = flow{
        try{
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            delay(1000)

            val heros: List<Hero> = try{
                service.getHeroStats()
            }catch (e: Exception){
                e.printStackTrace()
                emit(
                    DataState.Response(
                        uiComponent = UIComponent.Dialog(
                            title = "Network Data Error",
                            message = e.message ?: "Unknown error"
                        )
                    )
                )
                listOf()
            }

            cache.insert(heros)

            val cachedHeros = cache.selectAll()

            emit(DataState.Data(cachedHeros))

        }catch(e : Exception){
            e.printStackTrace()
            emit(
                DataState.Response<List<Hero>>(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        message = e.message ?: "Unknown error"
                    )
                )
            )
        }finally{
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}