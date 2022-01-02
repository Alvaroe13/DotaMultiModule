package com.alvaro.hero_interactors

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.core.domain.UIComponent
import com.alvaro.hero_datasource.cache.HeroCache
import com.alvaro.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHeroDetails(
    private val cache : HeroCache
) {


    fun execute(heroId: Int) : Flow<DataState<Hero>> = flow{
         try {
             emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

             // emit data from cache
             val cachedHero = cache.getHero(heroId)
                 ?: throw Exception("That hero does not exist in the cache.")

             emit(DataState.Data(cachedHero))

         }catch(e: Exception){
             e.printStackTrace()
             emit(DataState.Response<Hero>(
                 uiComponent = UIComponent.Dialog(
                     title = "Error",
                     message = e.message?: "Unknown error"
                 )
             ))
         }finally{
             emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
         }
    }


}