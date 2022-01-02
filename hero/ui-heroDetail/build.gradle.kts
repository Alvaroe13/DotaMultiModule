apply {
    from("$rootDir/android-library-build.gradle")
}


dependencies {
    "implementation"(project(path = Modules.core))
    "implementation"(project(path = Modules.heroDomain))
    "implementation"(project(path = Modules.heroInteractors))


    "implementation"(Coil.coil)
}