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

    shadowJar {
        archiveBaseName.set("AkaniCore")
        archiveVersion.set("")
        archiveClassifier.set("paper")
    }

    runVelocity {
        velocityVersion("3.3.0-SNAPSHOT")
    }

}