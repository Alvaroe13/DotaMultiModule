package com.alvaro.hero_datasource_test.network

import com.alvaro.hero_datasource.network.EndPoints
import com.alvaro.hero_datasource.network.HeroService
import com.alvaro.hero_datasource.network.HeroServiceImpl
import com.alvaro.hero_datasource_test.network.data.HeroDataEmpty
import com.alvaro.hero_datasource_test.network.data.HeroDataMalformed
import com.alvaro.hero_datasource_test.network.data.HeroDataValid
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*

class HeroServiceFake private constructor() {

    companion object Factory {

        private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
        private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

        fun build(
            type: HeroServiceResponseType
        ): HeroService {

            val client = HttpClient(MockEngine) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(
                        kotlinx.serialization.json.Json {
                            ignoreUnknownKeys = true // if the server sends extra fields, ignore them
                        }
                    )
                }
                engine {
                    addHandler { request ->
                        when (request.url.fullUrl) {
                            EndPoints.HERO_STATS -> {
                                val responseHeaders = headersOf(
                                    "Content-Type" to listOf("application/json", "charset=utf-8")
                                )
                                when(type){
                                    is HeroServiceResponseType.EmptyList -> {
                                        respond(
                                            content = HeroDataEmpty.data,
                                            status = HttpStatusCode.OK,
                                            headers = responseHeaders
                                        )
                                    }
                                    is HeroServiceResponseType.MalformedData -> {
                                        respond(
                                            content = HeroDataMalformed.data,
                                            status = HttpStatusCode.OK,
                                            headers = responseHeaders
                                        )
                                    }
                                    is HeroServiceResponseType.GoodData -> {
                                        respond(
                                            content = HeroDataValid.data,
                                            status = HttpStatusCode.OK,
                                            headers = responseHeaders
                                        )
                                    }
                                    is HeroServiceResponseType.Http404 -> {
                                        respond(
                                            content = HeroDataEmpty.data,
                                            status = HttpStatusCode.NotFound,
                                            headers = responseHeaders
                                        )
                                    }
                                }
                            }
                            else -> error("Unhandled ${request.url.fullUrl}")
                        }
                    }
                }
            }

            return HeroServiceImpl(client)
        }
    }
}