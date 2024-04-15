

dependencies {
    compileOnly(libs.paper)
    api(project(":api"))
    api(project(":common"))
    compileOnly(libs.vault)

}

tasks.test {
    useJUnitPlatform()
}