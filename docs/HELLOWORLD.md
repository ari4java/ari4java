# Hello ARI World with ari4java

This tutorial aims at showing a number of things:

* Setting up Asterisk for working with ARI
* Connecting to ARI
* Running a simple call
* Showing how you could make it all non-blocking

Enough to get us started!

## Asterisk Setup
Edit `/etc/asterisk/ari.conf` and create a user `ari4java` that we will use for connecting.
```
[general]
enabled = yes

[ari4java]
type = user
read_only = no
password = yothere
password_format = plain
```
Edit `/etc/asterisk/http.conf` and turn on the embedded HTTP server.
```
[general]
enabled=yes
bindaddr=0.0.0.0  
bindport=8088
```
Reload Asterisk and connect to its CLI; if all goes well you should see:
```
localhost*CLI> ari show users
r/o?  Username
----  --------
No    ari4java
```

## Java Code

In order to get started quickly with the ari4java library we suggest using gradle,
check the **Using the library** section in the [README](https://github.com/l3nz/ari4java/blob/master/README.md)

First create an ARI object that acts as your master connector.
```java
ARI ari = ARI.build("http://10.10.5.41:8088/", "test-app", "ari4java", "yothere", AriVersion.IM_FEELING_LUCKY);
```

The easiest thing we can do is a synchronous query to list active channels.
```java
List<Channel> channels = ari.channels().list().execute();
```
Please note that what we get back is Java objects, not just JSON blobs.
Everything is strongly typed.

A non-blocking way is also available by supplying a callback to the execute.
```java
ari.channels().list().execute(new AriCallback<List<Channel>>() {
    @Override
    public void onSuccess(List<Channel> result) {
        // code to handle the result ...
    }
    @Override
    public void onFailure(RestException e) {
        e.printStackTrace();
    }
});
```

A working example can be found under the examples tree, as 
in https://github.com/l3nz/ari4java/blob/master/examples/helloworld/HelloAriWorld.java

Happy hacking!
