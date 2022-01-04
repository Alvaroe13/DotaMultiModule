package com.alvaro.ui_herolist.ui

import com.alvaro.core.domain.UIComponentState
import com.alvaro.hero_domain.HeroAttribute
import com.alvaro.hero_domain.HeroFilter

sealed class HeroListEvents {
    object GetHerosEvent : HeroListEvents()
    object FilterHeros : HeroListEvents()
    data class SearchHeroByName(val heroName: String) : HeroListEvents()
    data class UpdateHeroFilter(val heroFilter: HeroFilter) : HeroListEvents()
    data class UpdateFilterDialogState(val uiComponentState: UIComponentState): HeroListEvents()
    data class UpdateAttributeFilter( val heroAttribute: HeroAttribute): HeroListEvents()
}