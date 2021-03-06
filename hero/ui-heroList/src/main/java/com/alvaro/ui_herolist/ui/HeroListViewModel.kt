package com.alvaro.ui_herolist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.Queue
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.domain.UIComponentState
import com.alvaro.core.util.Logger
import com.alvaro.hero_domain.HeroAttribute
import com.alvaro.hero_domain.HeroFilter
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

    fun triggerEvent(event: HeroListEvents) {
        logger.log("triggerEvent= ${event}")
        when (event) {
            is HeroListEvents.GetHerosEvent -> {
                getHeros()
            }
            is HeroListEvents.FilterHeros -> {
                filterHeros()
            }
            is HeroListEvents.SearchHeroByName -> {
                logger.log("triggerEvent search by name= ${event.heroName} ")
                updateHeroNameQuery(event.heroName)
            }
            is HeroListEvents.UpdateHeroFilter -> {
                updateHeroFilter(event.heroFilter)
            }
            is HeroListEvents.UpdateFilterDialogState -> {
                updateFilterDialogState(dialogState = event.uiComponentState)
            }
            is HeroListEvents.UpdateAttributeFilter -> {
                updateAttributeFilter(event.heroAttribute)
            }
            is HeroListEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }

    private fun updateAttributeFilter(heroAttribute: HeroAttribute) {
        state.value = state.value.copy(primaryAttribute = heroAttribute)
        filterHeros()
    }

    private fun updateFilterDialogState(dialogState: UIComponentState) {
        state.value = state.value.copy(filterDialogState = dialogState)
    }

    private fun updateHeroFilter(heroFilter: HeroFilter) {
        state.value = state.value.copy(heroFilter = heroFilter)
        filterHeros()
    }

    private fun updateHeroNameQuery(heroNameQuery: String) {
        state.value = state.value.copy(heroNameQuery = heroNameQuery)
    }

    // logic that filters list based on filter options selected
    private fun filterHeros() {
        val filteredList = filterHeros.execute(
            currentList = state.value.heros,
            heroNameQuery = state.value.heroNameQuery,
            heroFilter = state.value.heroFilter,
            attributeFilter = state.value.primaryAttribute
        )
        state.value = state.value.copy(filteredHeros = filteredList)
    }

    private fun getHeros() {
        getHeros.execute().onEach { dataState ->
            when (dataState) {
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            appendToMessageQueue( uiComponent = dataState.uiComponent )
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


    private fun appendToMessageQueue(uiComponent: UIComponent){
        val queue = state.value.messageQueue
        queue.add(uiComponent)
        state.value = state.value.copy(messageQueue = Queue(mutableListOf())) // force recompose
        state.value = state.value.copy(messageQueue = queue)
    }

    private fun removeHeadFromQueue(){
        try{
            val messageQueue = state.value.messageQueue
            messageQueue.remove()
            state.value = state.value.copy(messageQueue = Queue(mutableListOf())) // force recompose
            state.value = state.value.copy( messageQueue = messageQueue)
        }catch(e : Exception){
            logger.log("Nothing to remove from message queue")
        }
    }
}