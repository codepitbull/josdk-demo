import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("com.google.cloud.tools.jib") version "3.3.1"
}

group = "de.codepitbull.josdk.demo"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.4.0"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "de.codepitbull.josdk.demo.starter.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-core")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}

jib {
  from {
    image = "gcr.io/distroless/java17-debian11"
    platforms {
      platform {
        architecture = "amd64"
        os = "linux"
      }
      // We want to build multi-platform images by default. Can be disabled for CI.
      if (project.hasProperty("buildMultiArch") && project.property("buildMultiArch") == "true") {
        platform {
          architecture = "arm64"
          os = "linux"
        }
      }
    }
    setAllowInsecureRegistries(true)
  }
  to {
    image = "josdk-demo/starter"
  }
  container {
    mainClass = "io.vertx.core.Launcher"
    args = listOf("run", "de.codepitbull.josdk.demo.starter.MainVerticle")
    ports = listOf("8888")
  }
}

