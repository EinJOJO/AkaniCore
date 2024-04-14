dependencies {
    compileOnly(libs.adventure)
    compileOnly(libs.minimessage)

}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "it.einjojo.nucleoflex"
            artifactId = "api"
            version = "1.1"

            from(components["java"])
        }
    }
}
