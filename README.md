ari4java
========

The Asterisk REST Interface (ARI) bindings for Java.

 [ ![Download](https://api.bintray.com/packages/lenz/maven/ari4java/images/download.png) ](https://bintray.com/lenz/maven/ari4java/_latestVersion)

Description
-----------

ARI is an interface available on Asterisk 12/13 that lets you write applications
that run externally and control call flow through REST calls while receiving
events on a websocket.

In order to support different versions of the API, what we do is we maintain concrete implementations
for each version of the API, but we also have general interfaces that are used to work with objects
across different versions.

**Getting started?**

* Start from the "Hello ARI World" tutorial at https://github.com/l3nz/ari4java/blob/master/docs/HELLOWORLD.md
* Read our FAQs at https://github.com/l3nz/ari4java/blob/master/docs/FAQ.md
* Want to run a ready-made Asterisk image? This will make your life easier when developing! get
  yours from https://github.com/l3nz/whaleware/tree/master/examples/asterisk-load-test


**Tip:** join [our ari4java Google+ group](https://plus.google.com/u/0/communities/116130645492865479649) for news, help and plain bouncing of ideas around. 

Using the library
=================

If you use Gradle (or any tool using Maven dependencies) you can simply declare the lib as:


	repositories {
		mavenCentral()
	    mavenRepo(url: 'http://jcenter.bintray.com') 
	}


	dependencies {
	    compile 'ch.loway.oss.ari4java:ari4java:0.4.2'
	}

This will download the package and all required dependencies.

Building
========

The code here is partially hand-written and partially generated out of Swagger definitions.

* "classes/" contains Java code to be released (manually and automatically generated). All automatically
generated classes are under "ch.loway.oss.ari4vaja.generated". They should not be hand-edited. 
* "tests/" contains test cases for "classes/"
* "codegen/" contains the Java code that creates auto-generated classes.
* "codegen-data/" contains Swagger models from different versions of the interface (copied from Asterisk).

Creating Java code out of Swagger definitions
---------------------------------------------

In order to run codegen (class ch.loway.oss.ari4java.codegen.run), you need the following libraries:

- jackson-core-2.2.2
- jackson-databind-2.2.2
- jackson-annotations-2.2.2

Testing and packaging
---------------------

The easiest tway to build is simply using the Gradle script supplied.

		gradle clean build

This will compile, test and package the current version. It will not run the code generator (for the moment at least).
You'll find the resulting jar file under 'build/libs'.

Running
-------

The project requires:

- jackson-core-2.2.2
- jackson-databind-2.2.2
- jackson-annotations-2.2.2
- netty-all-4.0.25-Final

Status
------
 
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

or make your life easier by using the convenience method supplied in AriFactory.
		
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
  you need to pass only one parameter - see https://github.com/l3nz/ari4java/issues/10
* Misc open bugs here: https://github.com/l3nz/ari4java/issues?q=is%3Aopen


Useful links
------------

* Asterisk 13 docs: https://wiki.asterisk.org/wiki/display/AST/Asterisk+13+Documentation
* Asterisk 13 ARI docs: https://wiki.asterisk.org/wiki/display/AST/Asterisk+13+ARI
* Asterisk 12 docs: https://wiki.asterisk.org/wiki/display/AST/Asterisk+12+Documentation
* Official ARI docs for Asterisk 12: https://wiki.asterisk.org/wiki/display/AST/Asterisk+12+ARI
* ari4java community on Google+: https://plus.google.com/u/0/communities/116130645492865479649
* Asterisk-app-dev archives: http://lists.digium.com/pipermail/asterisk-app-dev/


Similar & Interesting projects
------------------------------

* AstAryPy - a Python library - https://github.com/asterisk/ast-ari-py
* AsterNET.ARI	- C# / .NET - https://github.com/skrusty/AsterNET.ARI
* node-ari-client - JavaScript (node) - https://github.com/asterisk/node-ari-client
* phpari - PHP - http://www.phpari.org/
* asterisk-ari-client - Ruby - https://github.com/svoboda-jan/asterisk-ari


Licensing
---------

The library is released under the GNU LGPL (see LICENSE file).
Files under codegen-data come from the Asterisk project and are licensed under the GPLv2 (see LICENSE.asterisk file therein).
They are only used to build the classes and are not distribuited in any form with Ari4Java.

