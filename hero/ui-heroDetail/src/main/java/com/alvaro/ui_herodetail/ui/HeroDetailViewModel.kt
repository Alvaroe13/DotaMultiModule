package com.alvaro.ui_herodetail.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.Queue
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.util.Logger
import com.alvaro.hero_interactors.GetHeroDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HeroDetailViewModel
@Inject
constructor(
    private val getHeroDetails: GetHeroDetails,
    private val savedStateHandle: SavedStateHandle,
    @Named("heroDetailsLogger") private val logger: Logger
) : ViewModel() {


    val state: MutableState<HeroDetailState> = mutableStateOf(HeroDetailState())

    init {
        savedStateHandle.get<Int>("heroId")?.let { heroId ->
            triggerEvent(events = HeroDetailEvents.GetHeroDetails(heroId = heroId))
        } ?: throw IllegalArgumentException("heroId in HeroDetails VM is null")

    }


    fun triggerEvent(events: HeroDetailEvents) {
        when (events) {
            is HeroDetailEvents.GetHeroDetails ->{
                getHeroDetails(heroId = events.heroId)
            }
            is HeroDetailEvents.OnRemoveHeadFromQueue ->{
                removeHeadFromQueue()
            }
        }
    }

    private fun getHeroDetails(heroId: Int) {
        getHeroDetails.execute(heroId = heroId).onEach { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Data -> {
                    state.value = state.value.copy(hero = dataState.data)
                }
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            appendToMessageQueue(uiComponent = dataState.uiComponent)
                        }
                        is UIComponent.None -> {
                            logger.log(" error= ${(dataState.uiComponent as UIComponent.None).message}")
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun appendToMessageQueue(uiComponent: UIComponent) {
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