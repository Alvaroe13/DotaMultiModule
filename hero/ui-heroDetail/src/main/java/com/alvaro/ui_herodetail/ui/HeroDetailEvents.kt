package com.alvaro.ui_herodetail.ui

sealed class HeroDetailEvents {

    data class GetHeroDetails(val heroId: Int) : HeroDetailEvents()

    object OnRemoveHeadFromQueue : HeroDetailEvents()

}
