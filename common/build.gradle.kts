

dependencies {
    api(project(":api"))
    api(libs.jedis)
    implementation(libs.guava)
    implementation(libs.gson)
    implementation(libs.boostedyaml)
    implementation(libs.caffeine)
    implementation(libs.hikari)
    compileOnly(libs.adventure)
    compileOnly(libs.luckperms )
}
