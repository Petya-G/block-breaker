import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

configurations.matching { it.name.contains("Artifact") }.configureEach {
    attributes {
        val os = DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName()
        val arch = DefaultNativePlatform.getCurrentArchitecture().name
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class, Usage.JAVA_RUNTIME))
        attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named(OperatingSystemFamily::class, os))
        attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named(MachineArchitecture::class, arch))
    }
}

plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    kotlin("jvm") version "2.1.10"
}

group = "blockbreaker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

javafx {
    version = "21"
    modules = listOf("javafx.controls")
}

application.mainClass = "blockbreaker.MainKt"

tasks.register<Jar>("fatJar") {
    archiveClassifier.set("fat")

    // Include compiled classes
    from(sourceSets.main.get().output)

    // Include runtime dependencies
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })

    manifest {
        attributes(
            "Main-Class" to "blockbreaker.MainKt"
        )
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}