ari4java
========

Asterisk ARI interface bindings for Java

Description
-----------

In order to support different versions of the API, what we do is we maintain concrete implementations
for each version of the API, but we also have general interfaces that are used to work with objects
across different versions.



Buiding/testing
---------------

The code here is partially hand-written and partially generated out of Swagger definitions.

"classes/" contains Java code to be released (manually and automatically generated). All automatically
generated classes are under "ch.loway.oss.ari4vaja.generated". They should not be hand-edited. 

"tests/" contains test cases for "classes/"

"codegen/" contains the Java code that creates auto-generated classes.

"codegen-data/" contains Swagger models from different versions of the interface (copied from Asterisk).

In order to run codegen (class ch.loway.oss.ari4java.codegen.run), you need the following libraries:

- jackson-core-2.2.2
- jackson-databind-2.2.2
- jackson-annotations-2.2.2

I expect a Gradle build script soon.


Status
------

* 13.10.18 - Auto-generates all classes and compiles them.


To be done
----------

Parameters that could be multiple are handled as only one item. I would like to have 
both ways, so that you do not have to create a List in the very common case that 
you need to pass only one parameter.

JSON deserializer is still missing. I am unsure of the way to deserialize a list of things in Jackson.

HTTP interface is still missing.

WebSocket interface is still missing.

Tests are still missing.

Factory to build objects is missing.

Events returning Object are handled as a String. 



