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
    shadowJar {
        //minimize()
    }

    runServer {
        minecraftVersion("1.20.4")
    }
}


