package com.alvaro.hero_interactors

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.ui.Logger
import com.alvaro.hero_datasource.network.HeroService
import com.alvaro.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHeros(
    private val service : HeroService,
    private val logger : Logger? = null
) {

    fun execute() : Flow<DataState<List<Hero>>> = flow{
        try{
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

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

            emit(DataState.Data(heros))

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