plugins {
    id("java")
}

group = "com.hytaleeco"
version = "1.0.0"

repositories {
    mavenCentral()
}

val useCiStubs = project.findProperty("ci") == "true"

if (useCiStubs) {
    sourceSets {
        main {
            java {
                srcDir("src/stubs/java")
            }
        }
    }
}

dependencies {
    if (!useCiStubs) {
        compileOnly(files("libs/HytaleServer.jar"))
    }
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
