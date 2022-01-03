package com.alvaro.hero_domain

import com.alvaro.core.domain.FilterOrder

sealed class HeroFilter(val uiValue: String) {

    data class Hero(
        val order: FilterOrder = FilterOrder.Descending
    ): HeroFilter( uiValue = "Hero")

    data class ProWins(
        val order: FilterOrder = FilterOrder.Ascending
    ) : HeroFilter(uiValue = "Pro win-rate")
}
