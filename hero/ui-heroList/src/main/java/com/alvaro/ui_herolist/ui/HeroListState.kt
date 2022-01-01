package com.alvaro.ui_herolist.ui

import com.alvaro.core.domain.ProgressBarState
import com.alvaro.hero_domain.Hero

data class HeroListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val heros: List<Hero> = listOf()
)