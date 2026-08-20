pluginManagement {
    repositories {
        google()
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

// Note: ASCII name here to avoid encoding issues in Gradle tooling;
// the user-facing app name ("StällFit") is set in app/src/main/res/values/strings.xml
rootProject.name = "StallFit"
include(":app")
include(":wear")
