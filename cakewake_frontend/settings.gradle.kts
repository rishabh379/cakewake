pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "cakewake"
include(":app")
include(":core")
include(":features:auth")
project(":features:auth").projectDir = file("features/auth")
include(":features:onboarding")
project(":features:onboarding").projectDir = file("features/onboarding")
include(":features:cake_customization")
project(":features:cake_customization").projectDir = file("features/cake_customization")
include(":features:home")
project(":features:home").projectDir = file("features/home")
include(":features:feed")
project(":features:feed").projectDir = file("features/feed")
