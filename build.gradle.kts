plugins {
    id("java-library")
    id("maven-publish")
}

version = "1.4.0"

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    group = "it.einjojo.akani.core"
    version = rootProject.version

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.panda-lang.org/releases")
        maven("https://repo.akani.dev/releases")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://mvn.lumine.io/repository/maven-public/")
        maven("https://libraries.minecraft.net/")
    }

    //publishing {
    //    repositories {
    //        maven {
    //            name = "AkaniDev"
    //            url = uri("https://repo.akani.dev/releases")
    //            credentials {
    //                username = providers.gradleProperty("AKANI_REPO_USER").get()
    //                password = providers.gradleProperty("AKANI_REPO_PASS").get()
    //            }
    //        }
    //    }
    //}
}

tasks {
    jar {
        enabled = false
    }

}


subprojects {

    tasks.withType<Jar>() {

    }

    java {
        withSourcesJar()
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        // options
        options.encoding = "UTF-8"
        options.isIncremental = true
    }


}
