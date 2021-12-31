package com.alvaro.hero_datasource.network

import com.alvaro.hero_domain.Hero
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface HeroService {

    suspend fun getHeroStats() : List<Hero>

    //builds instance of this service
    companion object Factory{
        fun build(): HeroService{
            return HeroServiceImpl(
                httpClient = HttpClient(Android){
                    install(JsonFeature){
                        serializer = KotlinxSerializer(
                            kotlinx.serialization.json.Json {
                                ignoreUnknownKeys = true // if the server returns extra fields, ignore them
                            }
                        )
                    }

                    install(Logging){
                        level = LogLevel.ALL
                    }
                }
            )
        }
    }


}