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

Running
-------

The project requires:

- jackson-core-2.2.2
- jackson-databind-2.2.2
- jackson-annotations-2.2.2
- netty-all-4.0.11-Final

Status
------

* 13.10.18 - Auto-generates all classes and compiles them.
* 13.10.21 - All objects are deserializable right out of JSON. Mesages can be deserialzed automatically.
* 13.11.26 - Netty.io based HTTP and WebSocket implementation, factory, sync and async methods
* 13.12.29 - Imported the definitions for Asterisk 12.0.0 - ARI 1.0.0 - a bit of code changes in the code generator

Using
-----

To use the Netty.io HTTP+WS implementation, include netty-all-4.0.12.Final.jar or newer in your classpath.

To initialize:

		ARI ari = new ARI();
		NettyHttpClient hc = new NettyHttpClient();
		hc.initialize("http://my-pbx-ip:8088/", "admin", "admin");
		ari.setHttpClient(hc);
		ari.setWsClient(hc);
		ari.setVersion(AriVersion.ARI_0_0_1);
		
Sample synchronous call:

		ActionApplications ac = ari.getActionImpl(ActionApplications.class);
		List<? extends Application> alist = ac.list();

Sample asynchronous call:

		ActionAsterisk aa = ari.getActionImpl(ActionAsterisk.class);
		aa.getGlobalVar("AMPMGRPASS", new AriCallback<Variable>() {
			@Override
			public void onSuccess(Variable result) {
				// Let's do something with the returned value
			}
			@Override
			public void onFailure(RestException e) {
				e.printStackTrace();
			}
		});
		
Sample WebSocket connection, waiting for events on hello and goodbye apps:

		ActionEvents ae = ari.getActionImpl(ActionEvents.class);
		ae.eventWebsocket("hello,goodbye", new AriCallback<Message>() {
			@Override
			public void onSuccess(Message result) {
				// Let's do something with the event
			}
			@Override
			public void onFailure(RestException e) {
				e.printStackTrace();
			}
		});
		Thread.sleep(5000); // Wait 5 seconds for events
		ari.closeAction(ae); // Now close the websocket
 
The Message object in the code above will be one of the message subtypes, 
you will have to introspect to find out which. 

To be done
----------

* Parameters that could be multiple are handled as only one item. I would like to have 
  both ways, so that you do not have to create a List in the very common case that 
  you need to pass only one parameter.
* Tests are still missing.
* Events returning Object are handled as a String. 
* New Swagger 1.3 format - see https://reviewboard.asterisk.org/r/2909/diff/

Useful links
------------

* Asterisk 12 docs: https://wiki.asterisk.org/wiki/display/AST/Asterisk+12+Documentation
* ari4java community on Google+: https://plus.google.com/u/0/communities/116130645492865479649
* Asterisk-app-dev archives: http://lists.digium.com/pipermail/asterisk-app-dev/


Similar & Interesting projects
------------------------------

* AstAryPy - a Python library - https://github.com/asterisk/ast-ari-py


Licensing
---------

The library is released under the GNU LGPL (see LICENSE file).
Files under codegen-data come from the Asterisk project and are licensed under the GPLv2 (see LICENSE.asterisk file therein).
They are only used to build the classes and are not distribuited in any form with Ari4Java.

