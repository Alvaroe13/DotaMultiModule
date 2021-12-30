apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    //kotlin plugin serialization for ktor
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}
dependencies {
    "implementation"(project(path = Modules.heroDomain))

    "implementation"(Ktor.core)
    "implementation"(Ktor.clientSerialization)
    "implementation"(Ktor.android)
}