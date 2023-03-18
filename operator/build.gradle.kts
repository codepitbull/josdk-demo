plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val bouncyCastleVersion: String by project
val testcontainersVersion: String by project
val assertjVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:quarkus-operator-sdk-bom:${quarkusPlatformVersion}"))
    implementation("io.quarkiverse.operatorsdk:quarkus-operator-sdk")
    implementation("io.quarkus:quarkus-arc")
    implementation("org.bouncycastle:bcpkix-jdk18on:${bouncyCastleVersion}")
    testImplementation("io.quarkus:quarkus-junit5")


    testImplementation("org.assertj:assertj-core:${assertjVersion}")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    testImplementation("org.testcontainers:k3s:${testcontainersVersion}")
}

group = "de.codepitbull.josdk"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
