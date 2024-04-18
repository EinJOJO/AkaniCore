plugins {
    id("java-library")
}

version = "1.0"

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    group = "it.einjojo.akani.core"
    version = rootProject.version

    repositories {
        mavenCentral()
        maven ("https://jitpack.io")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.panda-lang.org/releases")
    }

}

subprojects {

    tasks.withType<Jar>() {

    }
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        // options
        options.encoding = "UTF-8"
        options.isIncremental = true

    }

}
