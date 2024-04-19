
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
