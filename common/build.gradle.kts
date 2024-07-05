plugins {
    alias(libs.plugins.shadow)
}

dependencies {
    api(project(":api"))
    implementation(libs.jedis)
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    implementation(libs.guava)
    implementation(libs.gson)
    implementation(libs.boostedyaml)
    compileOnly(libs.caffeine)
    compileOnly(libs.hikari)
    compileOnly(libs.minimessage)
    compileOnly(libs.luckperms)
    compileOnly(libs.cloudnetbridge)
    compileOnly(libs.cloudnetwrapperjvm)
    compileOnly(libs.adventure)
    compileOnly(libs.luckperms)
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
}
