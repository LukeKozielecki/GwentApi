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

rootProject.name = "Gwent Api"
include(":app")
include(":feature:cardgallery")
include(":core:presentation")
include(":domain")
include(":feature:search")
include(":feature:auth")
include(":feature:carddetails")
include(":data")
include(":infrastructure")
include(":core:di")
include(":navigation")
include(":feature:cardartshowcase")
