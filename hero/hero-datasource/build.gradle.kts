apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    //kotlin plugin serialization for ktor
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}
dependencies {
    //---------- local modules created by us -------------//
    "implementation"(project(path = Modules.heroDomain))

    //--------- remote modules (third party libraries) --//
    "implementation"(Ktor.core)
    "implementation"(Ktor.clientSerialization)
    "implementation"(Ktor.android)
    "implementation"(Ktor.logger)
}