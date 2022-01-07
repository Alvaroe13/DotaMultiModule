package com.alvaro.hero_interactors

import com.alvaro.core.domain.FilterOrder
import com.alvaro.hero_domain.Hero
import com.alvaro.hero_domain.HeroAttribute
import com.alvaro.hero_domain.HeroFilter
import kotlin.math.round

class FilterHeros {


    fun execute(
        currentList: List<Hero>,
        heroNameQuery: String,
        heroFilter: HeroFilter,
        attributeFilter: HeroAttribute,
    ): List<Hero> {

        var filteredList: MutableList<Hero> = currentList.filter { hero ->
            hero.localizedName.lowercase().contains(heroNameQuery.lowercase())
        }.toMutableList()

        when (heroFilter) {
            is HeroFilter.Hero -> {
                when (heroFilter.order) {
                    is FilterOrder.Descending -> {
                        filteredList.sortByDescending { it.localizedName }
                    }
                    is FilterOrder.Ascending -> {
                        filteredList.sortBy { it.localizedName }
                    }
                }

            }

            is HeroFilter.ProWins -> {
                when (heroFilter.order) {
                    is FilterOrder.Descending -> {
                        filteredList.sortByDescending {
                            getWinRate(it.proWins.toDouble(), it.proPick.toDouble())
                        }
                    }
                    is FilterOrder.Ascending -> {
                        filteredList.sortBy {
                            getWinRate(it.proWins.toDouble(), it.proPick.toDouble())
                        }
                    }
                }
            }

        }

        when (attributeFilter) {
            is HeroAttribute.Strength -> {
                filteredList = filteredList.filter { it.primaryAttribute is HeroAttribute.Strength }
                    .toMutableList()
            }
            is HeroAttribute.Agility -> {
                filteredList = filteredList.filter { it.primaryAttribute is HeroAttribute.Agility }
                    .toMutableList()
            }
            is HeroAttribute.Intelligence -> {
                filteredList =
                    filteredList.filter { it.primaryAttribute is HeroAttribute.Intelligence }
                        .toMutableList()
            }
            is HeroAttribute.Unknown -> {//nothing}
            }
        }

        return filteredList
    }


    private fun getWinRate(proWins: Double, proPick: Double): Int {
        return if (proPick <= 0) {
            0
        } else {
            val winRate: Int =
                round(proWins / proPick * 100).toInt()
            winRate
        }
    }
}