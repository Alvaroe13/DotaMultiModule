package com.alvaro.ui_herolist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import coil.ImageLoader
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.ui_herolist.components.HeroListItem
import com.alvaro.ui_herolist.components.HeroListToolbar

@ExperimentalComposeUiApi
@Composable
fun HeroList(
    state: HeroListState,
    imageLoader: ImageLoader,
    navigateToDetailScreen: (Int) -> Unit,
    events: (HeroListEvents) -> Unit,
) {


    Box(modifier = Modifier.fillMaxSize()) {

        Column {

            HeroListToolbar(
                heroName = state.heroNameQuery,
                onHeroNameChanged = { heroName ->
                    events(HeroListEvents.SearchHeroByName(heroName = heroName))
                },
                onExecuteSearch = {
                    events(HeroListEvents.FilterHeros)
                },
                onShowFilterDialog = {

                }
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(state.filteredHeros) { hero ->
                    HeroListItem(
                        hero = hero,
                        imageLoader = imageLoader,
                        onSelectHero = { heroId: Int ->
                            navigateToDetailScreen(heroId)
                        },
                    )
                }
            }

        }

        if (state.progressBarState is ProgressBarState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

    }
}