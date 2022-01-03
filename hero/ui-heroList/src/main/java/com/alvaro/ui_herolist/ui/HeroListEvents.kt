package com.alvaro.ui_herolist.ui

sealed class HeroListEvents {

    object GetHerosEvent : HeroListEvents()

    object FilterHeros: HeroListEvents()

    data class SearchHeroByName(val heroName: String): HeroListEvents()
}