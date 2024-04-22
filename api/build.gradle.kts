dependencies {
    compileOnly(libs.adventure)
    compileOnly(libs.minimessage)
    api(libs.jedis)
    api(libs.hikari)
    compileOnly(libs.cloudnetwrapperjvm)
    compileOnly(libs.cloudnetbridge)
    compileOnly(libs.litecommands)
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "it.einjojo.akani.core"
            artifactId = "api"
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
