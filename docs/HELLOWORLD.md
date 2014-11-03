Hello ARI World with ari4java
=============================

This tutorial aims at showing a number of things:

* Setting up Asterisk for working with ARI
* Connecting to ARI
* Running a simple call
* Showing how you could make it all non-blocking

Enought to get us started!


Create an ARI account on Asterisk
---------------------------------


Edit /etc/asterisk/ari.conf and create a user "ari4java" that we will use for connecting.

	[general]
	enabled = yes

	[ari4java]
	type = user
	read_only = no
	password = yothere
	password_format = plain

Edit /etc/asterisk/http.conf and turn on the embedded HTTP server.

	[general]
	enabled=yes
	bindaddr=0.0.0.0  
	bindport=8088

Reload Asterisk and connect to its CLI; if all goes well you should see:

	localhost*CLI> ari show users
	r/o?  Username
	----  --------
	No    ari4java


Hello ARI World
---------------

In order to get started quickly with the ari4java library, we suggest
downloading all libs to a "libs" folder to be added to your local project:

	gradle downloadJars

Now create a Java class. The plainest thing we can do is getting a list of
all active channels on the server. This is simple to do and only requires
an HTTP round-trip, that for simplicty's sake will happen synchronously.

So we first create an ARI object that acts as your master connector.
You are going to use this to call each and every action.

	ARI ari = AriFactory.nettyHttp("http://10.10.5.41:8088/", "ari4java", "yothere", 
	        AriVersion.ARI_1_5_0);

The easiest thing we can do now is to query the list of active channels.
In order to do this we call the list() method on the Channels action.
All commands we can send are organized into a set of Actions.
            
    List<Channel> channels = ari.channels().list();

Please note that what we get back is Java objects, not just JSON blobs.
Everything is strongly typed.

A second thing to note is that this could be done in a non-blocking way as well,
just by calling the method 

	list(AriCallback<List<Channel>> callback);

instead of the plain list() method.

A working example can be found under the examples tree, as 
in https://github.com/l3nz/ari4java/blob/master/examples/helloworld/HelloAriWorld.java

Happy hacking!


