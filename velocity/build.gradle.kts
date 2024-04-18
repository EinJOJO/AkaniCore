plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.runvelocity)
}

group = "it.einjojo.akani"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":common"))
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    runVelocity {
        velocityVersion("3.2.0-SNAPSHOT")
    }

}