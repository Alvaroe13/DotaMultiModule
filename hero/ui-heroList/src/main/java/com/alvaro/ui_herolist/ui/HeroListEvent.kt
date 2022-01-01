package com.alvaro.ui_herolist.ui

sealed class HeroListEvent {
    object GetHerosEvent : HeroListEvent()
}