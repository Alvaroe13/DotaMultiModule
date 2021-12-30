apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    //---------- local modules created by us -------------//
    "implementation"(project(path = Modules.core))
    "implementation"(project(path = Modules.heroDataSource))
    "implementation"(project(path = Modules.heroDomain))

    //--------- remote modules (third party libraries) --//
    "implementation"(project(path = Kotlinx.coroutinesCore))
}