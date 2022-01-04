package com.alvaro.ui_herolist.ui

import com.alvaro.core.domain.Queue
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.domain.UIComponentState
import com.alvaro.hero_domain.Hero
import com.alvaro.hero_domain.HeroAttribute
import com.alvaro.hero_domain.HeroFilter

data class HeroListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val heros: List<Hero> = listOf(),
    val filteredHeros: List<Hero> = listOf(),
    val heroNameQuery: String = "", // name inserted in search bar
    val heroFilter: HeroFilter = HeroFilter.Hero(), // descending by default (take a look at HeroFilter class and you'll understand
    val primaryAttribute: HeroAttribute = HeroAttribute.Unknown,
    val filterDialogState: UIComponentState = UIComponentState.Hide,
    val messageQueue: Queue<UIComponent> = Queue(mutableListOf())
)