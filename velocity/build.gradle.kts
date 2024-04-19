plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.runvelocity)
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":common"))
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks {
    jar {
        enabled = false
    }
    build {
        dependsOn("shadowJar")
    }

    shadowJar {
        archiveBaseName.set("AkaniCore")
        archiveVersion.set("")
        archiveClassifier.set("velocity")
    }

    runVelocity {
        velocityVersion("3.3.0-SNAPSHOT")
    }

}