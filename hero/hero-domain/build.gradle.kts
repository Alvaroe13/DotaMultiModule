apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(path = Modules.core))
}