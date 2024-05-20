plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.luckperms)
    api(project(":api"))
    api(project(":common"))
    implementation(libs.caffeine)
    compileOnly(libs.vault)
    implementation(libs.fastboard)
    compileOnly(libs.placeholderapi)

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

        relocate("fr.mrmicky.fastboard", "it.einjojo.akani.core.paper.scoreboard.fastboard")
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


}