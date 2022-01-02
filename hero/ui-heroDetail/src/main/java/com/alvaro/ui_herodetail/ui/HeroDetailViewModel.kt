package com.alvaro.ui_herodetail.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaro.core.domain.DataState
import com.alvaro.hero_interactors.GetHeroDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel
@Inject
constructor(
    private val getHeroDetails: GetHeroDetails,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    val state: MutableState<HeroDetailState> = mutableStateOf(HeroDetailState())

    init {
        savedStateHandle.get<Int>("heroId")?.let { heroId ->
            triggerEvent(events = HeroDetailEvents.GetHeroDetails(heroId = heroId))
        } ?: throw IllegalArgumentException("heroId in HeroDetails VM is null")

    }


    fun triggerEvent(events: HeroDetailEvents) {
        when (events) {
            is HeroDetailEvents.GetHeroDetails -> getHeroDetails(heroId = events.heroId)
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
                    //TODO implement
                }
            }
        }.launchIn(viewModelScope)
    }
}