package com.alvaro.dotamultimodule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.alvaro.dotamultimodule.navigation.Screen
import com.alvaro.dotamultimodule.ui.theme.DotaMultiModuleTheme
import com.alvaro.ui_herodetail.ui.HeroDetail
import com.alvaro.ui_herolist.ui.HeroList
import com.alvaro.ui_herolist.ui.HeroListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DotaMultiModuleTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.HeroList.route,
                    builder = {

                        addHeroList( navController = navController, imageLoader = imageLoader )

                        addHeroDetail()
                    }
                )

            }
        }


    }


}

fun NavGraphBuilder.addHeroList(
    navController: NavController,
    imageLoader: ImageLoader
) {
    composable(
        route = Screen.HeroList.route
    ) {
        val viewModel: HeroListViewModel = hiltViewModel()
        HeroList(
            state = viewModel.state.value,
            imageLoader = imageLoader,
            navigateToDetailScreen = { heroId ->
                navController.navigate("${Screen.HeroDetail.route}/$heroId")
            }
        )
    }
}

fun NavGraphBuilder.addHeroDetail() {
    composable(
        route = Screen.HeroDetail.route + "/{heroId}",
        arguments = Screen.HeroDetail.arguments,
    ) { navBackStackEntry ->
        val heroId = navBackStackEntry.arguments?.get("heroId") as Int?
        HeroDetail(heroId = heroId)
    }
}

