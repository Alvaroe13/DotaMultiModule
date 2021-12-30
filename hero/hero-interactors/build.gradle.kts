apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

dependencies {
    //---------- local modules created by us -------------//
    "implementation"(project(path = Modules.core))
    "implementation"(project(path = Modules.heroDataSource))
    "implementation"(project(path = Modules.heroDomain))

    //--------- remote modules (third party libraries) --//
    "implementation"(Kotlinx.coroutinesCore)
}