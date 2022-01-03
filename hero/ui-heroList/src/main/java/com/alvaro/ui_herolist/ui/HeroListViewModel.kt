package com.alvaro.ui_herolist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.util.Logger
import com.alvaro.hero_domain.Hero
import com.alvaro.hero_interactors.FilterHeros
import com.alvaro.hero_interactors.GetHeros
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HeroListViewModel
@Inject
constructor(
    private val getHeros: GetHeros,
    @Named("heroListLogger") private val logger: Logger,
    private val filterHeros: FilterHeros
) : ViewModel() {

    val state: MutableState<HeroListState> = mutableStateOf(HeroListState())

    init {
        triggerEvent(HeroListEvents.GetHerosEvent)
    }

    fun triggerEvent(events: HeroListEvents) {
        logger.log("triggerEvent= ${events}")
        when (events) {
            is HeroListEvents.GetHerosEvent -> {
                getHeros()
            }
            is HeroListEvents.FilterHeros -> {
                filterHeros()
            }
            is HeroListEvents.SearchHeroByName -> {
                logger.log("triggerEvent search by name= ${events.heroName} ")
                updateHeroNameQuery(events.heroName)
            }
        }
    }

    private fun updateHeroNameQuery(heroNameQuery: String) {
        state.value = state.value.copy(heroNameQuery = heroNameQuery)
    }

    private fun filterHeros() {
       val filteredList =  filterHeros.exceute(
            currentList = state.value.heros,
            heroNameQuery = state.value.heroNameQuery,
            heroFilter = state.value.heroFilter,
            attributeFilter = state.value.primaryAttribute
        )
        state.value = state.value.copy( filteredHeros = filteredList)
    }

    private fun getHeros() {
        getHeros.execute().onEach { dataState ->
            when (dataState) {
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            logger.log((dataState.uiComponent as UIComponent.Dialog).message)
                        }
                        is UIComponent.None -> {
                            logger.log((dataState.uiComponent as UIComponent.None).message)
                        }
                    }
                }
                is DataState.Data -> {
                    state.value = state.value.copy(heros = dataState.data ?: listOf())
                    filterHeros()
                }
                is DataState.Loading -> {
                    //progressBarState.value = dataState.progressBarState
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
            }
        }.launchIn(viewModelScope)
    }
}