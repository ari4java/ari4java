# ARI4Java

The Asterisk REST Interface (ARI) bindings for Java.

[![Download](https://api.bintray.com/packages/ari4java/maven/ari4java/images/download.png)](https://bintray.com/ari4java/maven/ari4java/_latestVersion)
[![Build](https://github.com/l3nz/ari4java/workflows/ARI4Java%20Build/badge.svg)](https://github.com/l3nz/ari4java/workflows/ARI4Java%20Build/badge.svg)

## Description

ARI is an interface available on Asterisk 11+ that lets you write applications
that run externally and control call flow through REST calls while receiving
events on a websocket.

In order to support different versions of the API, what we do is we maintain concrete implementations
for each version of the API, but we also have general interfaces that are used to work with objects
across different versions.

### Getting started

Simply add the library and an SLF4J implementation to your package config, here is an example using Gradle
```
repositories {
    maven {
        url  "https://dl.bintray.com/ari4java/maven" 
    }
    jcenter()
}

dependencies {
    compile 'ch.loway.oss.ari4java:ari4java:+'
    compile 'org.apache.logging.log4j:log4j-slf4j-impl:2.13.0'
}
```
*The 1st repo declaration is temporary as we sort out moving from a private repo to an organization*

# Documentation
- [Releases](https://github.com/l3nz/ari4java/releases) and the [CHANGELOG](https://github.com/l3nz/ari4java/blob/master/CHANGELOG.md)
- The [Wiki](https://github.com/l3nz/ari4java/wiki) has some more info on how to use the project
    - [Examples]()
    - [Build]()

## Licensing
The library is released under the GNU LGPL (see [LICENSE](https://github.com/l3nz/ari4java/blob/master/LICENSE) file).
Files under codegen-data come from the Asterisk project and are licensed under the GPLv2 (see LICENSE.asterisk file therein).
They are only used to build the classes and are not distributed in any form with ARI4Java.
