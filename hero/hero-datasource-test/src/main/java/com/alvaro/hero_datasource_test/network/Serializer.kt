package com.alvaro.hero_datasource_test.network

import com.alvaro.hero_datasource.network.HeroDto
import com.alvaro.hero_datasource.network.toHero
import com.alvaro.hero_domain.Hero
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json = Json{
     ignoreUnknownKeys = true
}

fun serializeHeroData(jsonData: String): List<Hero>{
   return json.decodeFromString<List<HeroDto>>(jsonData).map{ it.toHero()}
}