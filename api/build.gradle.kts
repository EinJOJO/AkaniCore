dependencies {
    compileOnly(libs.adventure)
    compileOnly(libs.minimessage)
    compileOnly(libs.jedis)

}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "it.einjojo.nucleoflex"
            artifactId = "api"
            version = rootProject.version.toString()

            from(components["java"])
        }
    }
}
