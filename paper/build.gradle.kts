

dependencies {
    compileOnly(libs.paper)
    api(project(":api"))
    api(project(":common"))

}

tasks.test {
    useJUnitPlatform()
}