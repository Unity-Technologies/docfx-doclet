
# JavaDoc Doclet for DocFX

This doclet is designed to produce a YAML representation of the Javadoc-generated documentation, that can be integrated into [DocFX](https://dotnet.github.io/docfx/).

## Getting started

### JitPack (Recommended)

**Add JitPack repository to `settings.gradle.kts`**

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

**Add dependency to `build.gradle.kts`**

```kotlin
dependencies {
    implementation("com.github.Unity-Technologies:docfx-doclet:v1.1.1")
}
```

**Configure javadoc task in `build.gradle.kts`**

```kotlin
tasks.javadoc {
    options {
        (this as StandardJavadocDocletOptions).apply {
            doclet = "com.unity.doclet.DocFxDoclet"
            docletpath = configurations.runtimeClasspath.get().files.toList()
            addStringOption("outputpath", "${layout.buildDirectory.get()}/docfx-output")
        }
    }
}
```

**Or for Groovy DSL:**

**Add dependency to `build.gradle`:**
```groovy
dependencies {
    implementation 'com.github.Unity-Technologies:docfx-doclet:v1.1.1'
}
```

**Configure javadoc task in `build.gradle`:**
```groovy
javadoc {
    options.doclet = 'com.unity.doclet.DocFxDoclet'
    options.docletpath = configurations.runtimeClasspath.files.toList()
    options.addStringOption('outputpath', "${layout.buildDirectory.get()}/docfx-output")
}
```

### Local Maven

**Publish to local Maven repository:**

```sh
# Clone and publish the doclet
git clone https://github.com/Unity-Technologies/docfx-doclet.git
cd docfx-doclet
./gradlew publishToMavenLocal
```

**Add dependency to your project:**

**For Kotlin DSL (`build.gradle.kts`):**

```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.unity:docfx-doclet:1.1.1")
}
```

**Configure javadoc task:**

```kotlin
tasks.javadoc {
    options {
        (this as StandardJavadocDocletOptions).apply {
            doclet = "com.unity.doclet.DocFxDoclet"
            docletpath = configurations.runtimeClasspath.get().files.toList()
            addStringOption("outputpath", "${layout.buildDirectory.get()}/docfx-output")
        }
    }
}
```

#### Generate Documentation

Run the doclet:

```bash
./gradlew javadoc
```

The generated DocFX YAML files will be in `build/docfx-output/`.

### Standalone

Build using:

```bash
./gradlew jar uberJar
```

This will produce two JAR files, one without dependencies, and an uberJar with them bundled in. Javadoc can then be used with the command line parameters:

```sh
javadoc \
    -encoding UTF-8 \
    -docletpath ./target/docfx-doclet-1.0-SNAPSHOT-jar-with-dependencies.jar \
    -doclet com.unity.doclet.DocFxDoclet \
    -classpath <list of jar with dependencies> \
    -sourcepath ./src/test/java \
    -outputpath ./target/test-out \
    -excludepackages com\.msdn\..*:com\.ms\.news\..*  \
    -excludeclasses .*SomeClass:com\.ms\..*AnyClass \
    -subpackages com.unity.samples
```

#### Advanced Usage

For example, if we wanted to generate documentation for [JUnit-4.12 source code](https://mvnrepository.com/artifact/junit/junit/4.12), we would need to account for the fact that the library depends on `hamcrest-core-1.3`, therefore we would download this library, unpack the sources JAR and run the following command:

```sh
javadoc \
    -encoding UTF-8 \                              # Source files encoding
    -docletpath ./docfx-doclet-1.1.0-uberJar.jar \ # Set path to jar with doclet
    -doclet com.unity.doclet.DocFxDoclet \         # Set name of doclet class
    -cp ./hamcrest-core-1.3.jar \                  # Put dependencies into classpath
    -sourcepath ./junit-4.12-sources \             # Set localtion of jar with sources
    -outputpath ./test-out \                       # Set location of output files
    -subpackages org:junit                         # Subpackages to recursively load separated by ':'
```

#### Parameters

| Parameter         | Description |
|-------------------|----------------------------------------------------------------------|
| `encoding`        | Encoding for source files (_optional_).                              |
| `docletpath`      | Path to the doclet JAR file.                                         |
| `doclet`          | Doclet class name.                                                   |
| `classpath`       | List of dependencies to be included in the classpath (_optional_).   |
| `sourcepath`      | Location of the source code that needs to be documented.             |
| `outputpath`      | The location for the generated YAML files.                           |
| `excludepackages` | List of excluded packages, separated by a colon (`:`) (_optional_).  |
| `excludeclasses`  | List of excluded classes, separated by a colon (`:`) (_optional_).   |
| `subpackages`     | Subpackages to recursively load, separated by a colon (`:`).         |

## Serving DocFx documentation

1. Get DocFX. You can read about it on the [official site](https://dotnet.github.io/docfx/).
2. Initialize an empty docset, by calling: `docfx init -q`
3. Place the generated YAML files in the `api` folder in the generated docset.
4. Build the content in the folder by calling: `docfx`
5. Serve the content on a local web server: `docfx serve _site`

## Development Guidelines

- Check the tests with `./gradlew tests` before submitting any PRs.
- Format your commit messages using [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).
