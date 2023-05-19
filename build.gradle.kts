plugins {
    kotlin("jvm") version "1.8.20"
    `maven-publish`
}

group = "net.spaceblock.utils"
version = properties["version"] as String

val kotlinCoroutinesVersion = "1.7.1"
val springVersion = "6.0.9"
val koTestVersion = "5.6.2"
val guiceVersion = "6.0.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    implementation("org.apache.logging.log4j:log4j-core:2.20.0")

    // KReflection
    implementation(kotlin("reflect"))

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$kotlinCoroutinesVersion")

    // Spring
    implementation("org.springframework:spring-context:$springVersion")
    implementation("org.springframework:spring-beans:$springVersion")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.19:3.1.0")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$koTestVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "github"
            url = uri("https://maven.pkg.github.com/spaceblocknet/mc-kotlin")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}
