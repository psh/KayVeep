dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "KayVeep"

include(
    ":java:lib",
    ":groovy:lib"
)
