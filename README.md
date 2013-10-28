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
* 13.10.21 - All objects are deserializable right out of JSON. Mesages can be deserialzed automatically.


Using
-----

You'll have to roll your own HTTP client implementation.

In order to deserialze a Websocket event, you call:

   Message msg = action.deserializeEvent( jsonEvent, Message_impl_ari_0_0_1.class );

And then check the actual class of the event (too bad Java cannot use types in switch statements).
Make sure you use a concrete Message class as the root deserialization object.


To be done
----------

* Parameters that could be multiple are handled as only one item. I would like to have 
  both ways, so that you do not have to create a List in the very common case that 
  you need to pass only one parameter.
* HTTP client interface is still missing.
* WebSocket interface is still missing.
* Tests are still missing.
* Factory to build objects is missing.
* Events returning Object are handled as a String. 
* New Swagger 1.3 format - see https://reviewboard.asterisk.org/r/2909/diff/

Useful links
------------

* Asterisk 12 docs: https://wiki.asterisk.org/wiki/display/AST/Asterisk+12+Documentation
* ari4java community on Google+: https://plus.google.com/u/0/communities/116130645492865479649
* Asterisk-app-dev archives: http://lists.digium.com/pipermail/asterisk-app-dev/



Licensing
---------

The library is released under the GNU LGPL (see LICENSE file).
Files under codegen-data come from the Asterisk project and are licensed under the GPLv2 (see LICENSE.asterisk file therein).
They are only used to build the classes and are not distribuited in any form with Ari4Java.

