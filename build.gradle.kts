plugins {
    id("java-library")
    id("maven-publish")
}

version = "1.5.5-1"

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

    publishing {
        repositories {
            maven {
                name = "akani-repo"
                url = uri("https://repo.akani.dev/releases")
                credentials {
                    username = findProperty("AKANI_REPO_USER") as String? ?: System.getenv("AKANI_REPO_USER")
                    password = findProperty("AKANI_REPO_PASS") as String? ?: System.getenv("AKANI_REPO_PASS")
                }
            }
        }
    }
}

tasks {
    jar {
        enabled = false
    }
    register("print-akani-credentials") {
        doLast {
            println("AKANI_REPO_USER: ${providers.gradleProperty("AKANI_REPO_USER").get()}")
            println("AKANI_REPO_PASS: ${providers.gradleProperty("AKANI_REPO_PASS").get()}")
        }

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
        options.compilerArgs.add("-parameters")

    }


}
