
plugins {
    alias(libs.plugins.shadow)
}

dependencies {
    api(project(":api"))
    api(libs.jedis)
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    implementation(libs.guava)
    implementation(libs.gson)
    implementation(libs.boostedyaml)
    implementation(libs.caffeine)
    implementation(libs.hikari)
    compileOnly(libs.cloudnetbridge)
    compileOnly(libs.cloudnetwrapperjvm)
    compileOnly(libs.adventure)
    compileOnly(libs.luckperms )
}

tasks {
    build {
        dependsOn("shadowJar")
    }
    shadowJar {
        //minimize()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "it.einjojo.akani.core"
            artifactId = "common"
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
