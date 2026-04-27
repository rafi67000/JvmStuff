plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    manifest {
        attributes["Premain-Class"] = "xyz.rafi67000.agent.JavacAgent"
        attributes["Agent-Class"] = "xyz.rafi67000.agent.JavacAgent"
        attributes["Can-Redefine-Classes"] = true
        attributes["Can-Retransform-Classes"] = true
    }
}