package com.alvaro.dotamultimodule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.ImageLoader
import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.util.Logger
import com.alvaro.dotamultimodule.ui.theme.DotaMultiModuleTheme
import com.alvaro.hero_domain.Hero
import com.alvaro.hero_interactors.HeroInteractors
import com.alvaro.ui_herolist.ui.HeroList
import com.alvaro.ui_herolist.ui.HeroListState
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    private val state: MutableState<HeroListState> = mutableStateOf(HeroListState())

    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        imageLoader = ImageLoader.Builder(applicationContext)
            .error(R.drawable.error_image)
            .placeholder(R.drawable.white_background)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .build()


        val getheros = HeroInteractors.build(
            sqlDriver = AndroidSqliteDriver(
                schema = HeroInteractors.schema,
                context = this,
                name = HeroInteractors.dbName,
            )
        ).getHeros
        val logger = Logger("GetHeroTest")

        getheros.execute().onEach { dataState ->
            when (dataState) {
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            logger.log((dataState.uiComponent as UIComponent.Dialog).message)
                        }
                        is UIComponent.None -> {
                            logger.log((dataState.uiComponent as UIComponent.None).message)
                        }
                    }
                }
                is DataState.Data -> {
                    state.value = state.value.copy(heros = dataState.data ?: listOf())
                }
                is DataState.Loading -> {
                    //progressBarState.value = dataState.progressBarState
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))


        setContent {
            DotaMultiModuleTheme {
                HeroList(
                    state = state.value,
                    imageLoader = imageLoader
                )
            }
        }


    }


}
