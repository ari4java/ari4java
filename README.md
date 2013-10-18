ari4java
========

Asterisk ARI interface bindings for Java


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





