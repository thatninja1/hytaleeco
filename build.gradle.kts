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
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
