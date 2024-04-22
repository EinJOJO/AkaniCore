plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
}

dependencies {
    compileOnly(libs.paper)
    api(project(":api"))
    api(project(":common"))
    implementation(libs.caffeine)
    compileOnly(libs.vault)

}
tasks {
    build {
        dependsOn("shadowJar")
    }

    jar {

    }
    shadowJar {
        //minimize()
        archiveBaseName.set("AkaniCore")
        archiveVersion.set("")
        archiveClassifier.set("paper")
    }

    runServer {
        minecraftVersion("1.20.4")
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(mapOf("version" to project.version.toString()))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "it.einjojo.akani.core"
            artifactId = "paper"
            version = rootProject.version.toString()


            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "AkaniDev"
            url = uri("https://repo.akani.dev/releases")
            credentials {
                username = System.getProperty("AKANI_REPO_USER")
                password = System.getProperty("AKANI_REPO_PASS")
            }
        }
    }
}