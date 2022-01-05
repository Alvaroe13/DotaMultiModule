apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(path= Modules.heroDomain))
    "implementation"(project(path= Modules.heroDataSource))

    "implementation"(Ktor.ktorClientMock)
    "implementation"(Ktor.clientSerialization)
}