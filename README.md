# ARI4Java

The Asterisk REST Interface (ARI) bindings for Java.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ari4java/ari4java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.ari4java/ari4java)
[![javadoc](https://javadoc.io/badge2/io.github.ari4java/ari4java/javadoc.svg)](https://javadoc.io/doc/io.github.ari4java/ari4java)
[![Build](https://github.com/ari4java/ari4java/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/ari4java/ari4java/actions?query=workflow%3A%22ARI4Java+Build%22)

## Description

ARI is an interface available on Asterisk 11+ that lets you write applications
that run externally and control call flow through REST calls while receiving
events on a websocket.

In order to support different versions of the API, what we do is we maintain concrete implementations
for each version of the API, but we also have general interfaces that are used to work with objects
across different versions.

### Getting started

Simply add the library and an SLF4J logger to your package config, here is an example using Gradle
```
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.ari4java:ari4java:+'
    implementation 'ch.qos.logback:logback-classic:1.2.10'
}
```

### Building from Source

The project uses code generation to create ARI bindings from Asterisk's Swagger API definitions. To build:

1. Clone the repository
```bash
git clone https://github.com/ari4java/ari4java.git
cd ari4java
```

2. Build (this will automatically generate ARI bindings)
```bash
./gradlew build
```

The code generator task runs automatically before compilation to ensure the ARI bindings are up to date.

Due to the sun setting of JCenter the jar is now publish through Sonatype to Maven Central but under a new groupId.
The groupId is now `io.github.ari4java` make sure you update your build files if you used `ch.loway.oss.ari4java`.

## Documentation
- The [CHANGELOG](https://github.com/ari4java/ari4java/blob/master/CHANGELOG.md)
- The [Wiki](https://github.com/ari4java/ari4java/wiki) has some more info on how to use the project
    - [Getting Started](https://github.com/ari4java/ari4java/wiki/Getting-Started)
    - [Examples](https://github.com/ari4java/ari4java/wiki/Examples)

## Licensing
The library is released under the GNU LGPL (see [LICENSE](https://github.com/ari4java/ari4java/blob/master/LICENSE) file).
Files under codegen-data come from the Asterisk project and are licensed under the GPLv2 (see LICENSE.asterisk file therein).
They are only used to build the classes and are not distributed in any form with ARI4Java.
