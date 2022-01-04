package com.alvaro.ui_herolist.ui

import androidx.compose.animation.ExperimentalAnimationApi
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
import com.alvaro.components.DefaultScreenUI
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.core.domain.UIComponentState
import com.alvaro.ui_herolist.components.HeroListFilterDialog
import com.alvaro.ui_herolist.components.HeroListItem
import com.alvaro.ui_herolist.components.HeroListToolbar

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun HeroList(
    state: HeroListState,
    imageLoader: ImageLoader,
    navigateToDetailScreen: (Int) -> Unit,
    events: (HeroListEvents) -> Unit,
) {

    DefaultScreenUI(
        progressBarState = state.progressBarState,
        content = {
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
                        events(HeroListEvents.UpdateFilterDialogState(uiComponentState = UIComponentState.Show))
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

                if (state.filterDialogState is UIComponentState.Show) {

                    HeroListFilterDialog(
                        heroFilter = state.heroFilter,
                        onUpdateHeroFilter = { heroFilter ->
                            events(HeroListEvents.UpdateHeroFilter(heroFilter))
                        },
                        onCloseDialog = {
                            events(HeroListEvents.UpdateFilterDialogState(uiComponentState = UIComponentState.Hide))
                        },
                        attributeFilter = state.primaryAttribute,
                        onUpdateAttributeFilter = { attribute ->
                            events(HeroListEvents.UpdateAttributeFilter(heroAttribute = attribute))
                        }
                    )
                }

            }
        }

    )
}