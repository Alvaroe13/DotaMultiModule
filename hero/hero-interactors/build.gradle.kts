apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

dependencies {
    //---------- local modules created by us PRODUCTION -------------//
    "implementation"(project(path = Modules.core))
    "implementation"(project(path = Modules.heroDataSource))
    "implementation"(project(path = Modules.heroDomain))

    //--------- remote modules (third party libraries) --//
    "implementation"(Kotlinx.coroutinesCore)

    //---------- local modules created by us TEST -------------//
    "testImplementation"(project(path = Modules.heroDataSourceTest))


    //--------- remote modules (third party libraries) --//
    "testImplementation"(Junit.junit4)
    "testImplementation"(Ktor.ktorClientMock)
    "testImplementation"(Ktor.clientSerialization)
}