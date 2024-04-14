

dependencies {
    api(project(":api"))
    api(libs.jedis)
    implementation(libs.guava)
    implementation(libs.gson)
    implementation(libs.caffeine)
    compileOnly(libs.adventure)
    compileOnly(libs.luckperms )
}
