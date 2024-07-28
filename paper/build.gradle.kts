plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
}



dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.luckperms)
    compileOnly(libs.hikari)
    api(project(":api"))
    api(project(":common"))
    //compileOnly(libs.caffeine)
    compileOnly(libs.vault)
    compileOnly(libs.akaniutils)
    implementation(libs.fastboard)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.viaversion)
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
        relocate("com.zaxxer.hikari", "it.einjojo.akani.core.hikari")
    }

    runServer {
        minecraftVersion("1.20.4")
    }


    processResources {
        filesMatching("plugin.yml") {
            expand(
                mapOf(
                    "version" to project.version.toString(),
                    "hikari" to libs.hikari.get(),
                    "caffeine" to libs.caffeine.get()
                )
            )
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