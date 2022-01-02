package com.alvaro.ui_herodetail.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun HeroDetail(
    state: HeroDetailState
){
    Text(text = "Hero name: ${state.hero?.localizedName}")
}