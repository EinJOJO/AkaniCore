plugins {
    id("java-library")
    id("maven-publish")
}

version = "1.1.7"

allprojects {
    apply(plugin = "java-library")
    group = "it.einjojo.akani.core"
    version = rootProject.version

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.panda-lang.org/releases")
        maven("https://repo.akani.dev/releases")
    }
}

tasks {
    jar {
        enabled = false
    }
}


subprojects {
    apply(plugin = "maven-publish")
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
