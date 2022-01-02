package com.alvaro.ui_herodetail.ui

import com.alvaro.core.domain.ProgressBarState
import com.alvaro.hero_domain.Hero

data class HeroDetailState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val hero : Hero? = null
)