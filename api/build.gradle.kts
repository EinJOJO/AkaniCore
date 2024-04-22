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

}
