plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
}

group = "xyz.rafi67000.titanspigot"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.rafi67000.xyz/private/")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("xyz.rafi67000.titanspigot3:API:3.0.0-SNAPSHOT") {
        isTransitive = false
    }
}

tasks {

    jar { enabled = false }

    processResources {
        val version = project.version
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }

    shadowJar {
        archiveClassifier = ""

        minimize()
        exclude("META-INF/")
        exclude("org/jetbrains/annotations/**")
        exclude("org/intellij/lang/annotations/**")

        relocate("kotlin", "xyz.rafi67000.titanspigot.debug.kotlin")
    }
}