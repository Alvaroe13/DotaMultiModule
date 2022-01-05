package com.alvaro.hero_datasource_test.cache

import com.alvaro.hero_domain.Hero

class HeroDatabaseFake {

    val heros: MutableList<Hero> = mutableListOf()
}