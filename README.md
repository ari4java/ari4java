# ari4java

The Asterisk REST Interface (ARI) bindings for Java.

[![Download](https://api.bintray.com/packages/ari4java/maven/ari4java/images/download.png)](https://bintray.com/ari4java/maven/ari4java/_latestVersion)

## Description

ARI is an interface available on Asterisk 11+ that lets you write applications
that run externally and control call flow through REST calls while receiving
events on a websocket.

In order to support different versions of the API, what we do is we maintain concrete implementations
for each version of the API, but we also have general interfaces that are used to work with objects
across different versions.

## Using the library

If you use Gradle (or any tool using Maven dependencies) you can simply declare the lib as:
```
repositories {
    maven {
        url  "https://dl.bintray.com/ari4java/maven" 
    }
    mavenCentral()
    jcenter()
}

dependencies {
    compile 'ch.loway.oss.ari4java:ari4java:0.8.1'
}
```
This will download the package and all required dependencies.
*The 1st repo declaration is temporary as we sort out moving from a private repo to an organization*

### Getting started?

- Start from the ["Hello ARI World"](https://github.com/l3nz/ari4java/blob/master/docs/HELLOWORLD.md) tutorial at 
- Read our [FAQs](https://github.com/l3nz/ari4java/blob/master/docs/FAQ.md)
- Check out more detailed [USAGE](https://github.com/l3nz/ari4java/blob/master/docs/USAGE.md)
- Want to run a ready-made Asterisk image? This will make your life easier when developing! get
  yours from https://github.com/l3nz/whaleware/tree/master/examples/asterisk-load-test

## Building
The code here is partially hand-written and partially generated out of Swagger definitions.

### Folders:
- "src/main/java" contains Java code to be released (manually and automatically generated). 
- "src/main/generated" Are all automatically generated classes, they should not be hand-edited. 
- "src/test/java/" contains test cases for "src/main/java"
- "codegen/" is a gradle sub-project that generates code in "src/main/generated"

### Build and test
The easiest way to build is simply using the Gradle Wrapper script supplied.
```
./gradlew clean build
```
This will generate, compile, test and package the current version.
You'll find the resulting jar file under the `build/libs` folder.

## Status

* 20.01.25 - Rel 0.8.1. Java 8 Compatibility, better exception messages
* 19.12.23 - Rel 0.8.0. :exclamation: **!! BREAKING CHANGES !!** API Actions signatures only contain manditory params and returns a Request object which follows the builder/factory pattern
* 19.12.22 - Rel 0.7.0. Treat `fields` as `fields` not `variables` in body parameters; fix `ActionRecordings.getStoredFile()` for `byte[]` & add `ARI.mailboxes()`
* 19.11.07 - Rel 0.6.1. Codegen bug fixes for object and rebuild with latest ARI api docs
* 19.10.13 - Rel 0.6.0. Project restructure and include all past and present versions of ARI
* 19.04.03 - Rel 0.5.1. Goodbye Naama!
* 19.01.07 - Support java 9 (#108), code generation from gradle(#110), fixed unsubscribing from application correctly(#59), added event interface inheritance(#106) rel 0.5.0
* 17.12.19 - Added support for ARI 3.0.0 (#78)
* 17.02.04 - Added support for ARI 2.0.0 (#62) and quicker deserialization (#63)
* 16.11.30 - Fixes on Graham's AutoReconnect patch - #60 - rel 0.4.3 
* 16.10.21 - Fixing #55 and #57 - rel 0.4.2
* 16.10.17 - Graham's AutoReconnect patch #52 - rel 0.4.1
* 16.08.31 - Added support for ARI 1.10.0 (Asterisk 14) and some bug fixes - release 0.4.0
* 16.01.30 - Added support for ARI 1.9.0 - release 0.3.4
* 15.09.23 - Fixed issue with 201 statuses (bug #33) - release 0.3.3
* 15.09.19 - Added support for ARI 1.8.0 (bug #32) - release 0.3.2
* 15.03.20 - Disconnected ARI WS now throws an exception - se bug #28 - release 0.3.1
* 15.03.11 - Added support for ARI 1.7.0 (bug #28) - release 0.3.0
* 15.01.17 - Added support for ARI 1.6.0 (bug #24) - release 0.2.3 - compiles with Netty 4.0.25
* 14.11.01 - Added support for ARI 1.5 (bug #9)
* 14.10.30 - Added ARI bindings for 1.5.0 as coming from the official Asterisk 13.0.0 release
* 14.01.01 - Added a minimal application under tests/ class ch.loway.oss.ari4java.sandbox.sample to be used as a style laboratory. Look for UGLY tags. Rel 0.1.2.
* 13.12.30 - Added AriBuilder interfaces
* 13.12.29 - Imported the definitions for Asterisk 12.0.0 - ARI 1.0.0 - a bit of code changes in the code generator - Added the Gradle build script.
* 13.11.26 - Netty.io based HTTP and WebSocket implementation, factory, sync and async methods
* 13.10.21 - All objects are deserializable right out of JSON. Mesages can be deserialized automatically.
* 13.10.18 - Auto-generates all classes and compiles them.


## Useful links
* Asterisk REST Interface wiki: https://wiki.asterisk.org/wiki/pages/viewpage.action?pageId=29395573
* Asterisk 16 ARI docs: https://wiki.asterisk.org/wiki/display/AST/Asterisk+16+ARI
* Asterisk-app-dev archives: http://lists.digium.com/pipermail/asterisk-app-dev/


## Similar & Interesting projects
* AstAryPy - a Python library - https://github.com/asterisk/ast-ari-py
* AsterNET.ARI	- C# / .NET - https://github.com/skrusty/AsterNET.ARI
* node-ari-client - JavaScript (node) - https://github.com/asterisk/node-ari-client
* phpari - PHP - http://www.phpari.org/
* asterisk-ari-client - Ruby - https://github.com/svoboda-jan/asterisk-ari


## Licensing
The library is released under the GNU LGPL (see LICENSE file).
Files under codegen-data come from the Asterisk project and are licensed under the GPLv2 (see LICENSE.asterisk file therein).
They are only used to build the classes and are not distribuited in any form with Ari4Java.

