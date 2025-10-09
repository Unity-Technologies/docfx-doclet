import com.github.jk1.license.render.TextReportRenderer

plugins {
    id("java")
    id("jacoco")
    id("com.github.jk1.dependency-license-report") version "2.9"
}

group = "com.unity"
version = "1.1.0"

java {
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.19.0")
    implementation("org.apache.commons:commons-collections4:4.5.0")
    implementation("commons-io:commons-io:2.20.0")
    implementation(enforcedPlatform("com.fasterxml.jackson:jackson-bom:2.20.0"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.mockito:mockito-core:5.20.0")
    testImplementation("com.google.testing.compile:compile-testing:0.23.0")

    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")
}

layout.buildDirectory = file("output")

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf(
        "-Xlint:unchecked",
        "-Xlint:deprecation",
        "-Xlint:rawtypes",
        "-Xlint:cast",
        "-Xlint:finally"
    ))
    options.isIncremental = true
}

tasks.javadoc {
    options {
        doclet = "com.unity.doclet.DocFxDoclet"
        docletpath = configurations.compileClasspath.get().toList() + files("${layout.buildDirectory.get()}/classes/java/main")
        destinationDirectory = layout.buildDirectory.dir("doc").get().asFile
    }
}

licenseReport {
    configurations = arrayOf("runtimeClasspath")
    outputDir = layout.buildDirectory.dir("reports/licenses").get().asFile.toString()
    renderers = arrayOf(TextReportRenderer())
}

tasks.register<JavaExec>("runDocletOnSamples") {
    group = "documentation"
    description = "Run the doclet on the test sample classes"
    dependsOn(tasks.classes)

    classpath = configurations.runtimeClasspath.get() + files("${layout.buildDirectory.get()}/classes/java/main")
    mainClass.set("jdk.javadoc.internal.tool.Main")
    args(
        "-doclet", "com.unity.doclet.DocFxDoclet",
        "-sourcepath", "src/test/java",
        "-subpackages", "com.unity.samples",
        "-outputpath", "${layout.buildDirectory.get()}/doclet-output"
    )
}

tasks.register<Jar>("uberJar") {
    group = "build"
    description = "Assembles an uber JAR including runtime dependencies."

    archiveClassifier = "uber"
    setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    exclude("LICENSE*")

    dependsOn(tasks.named("generateLicenseReport"))
    from(layout.buildDirectory.dir("reports/licenses").get().asFile) {
        into("META-INF/LICENSES")
    }
    from("LICENSE") {
        into("META-INF/")
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)

    testLogging {
        events("passed", "skipped", "failed", "started")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("reports/coverage"))
    }

    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal() // 80% line coverage
            }
        }
    }
}

