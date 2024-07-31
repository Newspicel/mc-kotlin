plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

group = "dev.newspicel"
version = properties["version"] as String

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper.api)

    // KReflection
    implementation(libs.kotlin.reflect)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactive)

    // Guice
    implementation(libs.guice)
    implementation(libs.reflections)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockbukkit)
    testImplementation(libs.kotest)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
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
            url = uri("https://maven.pkg.github.com/newspicel/mc-kotlin")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}
