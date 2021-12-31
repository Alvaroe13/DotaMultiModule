apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    //kotlin plugin serialization for ktor
    //kotlin(KotlinPlugins.serialization) version Kotlin.version
    id(SqlDelight.plugin)
}
dependencies {
    //---------- local modules created by us -------------//
    "implementation"(project(path = Modules.heroDomain))

    //--------- remote modules (third party libraries) --//
    "implementation"(Ktor.core)
    "implementation"(Ktor.clientSerialization)
    "implementation"(Ktor.android)
    "implementation"(Ktor.logger)

    "implementation"(SqlDelight.runtime)
}
sqldelight{
    database("HeroDatabase"){
        packageName = "com.alvaro.hero_datasource.cache"
        sourceFolders = listOf("sqldelight")
    }
}