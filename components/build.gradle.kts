apply {
    from("$rootDir/android-library-build.gradle")
}


dependencies {
    "implementation"(project(path = Modules.core))
}