# Using ari4java

To initialize:
```java
ARI ari = ARI.build("http://my-pbx-ip:8088/", "app-name", "user", "pass", AriVersion.ARI_x_x_x);
```
`ARI_x_x_x` must be replaced with a valid version from `AriVersion` or you can use `AriVersion.IM_FEELING_LUCKY`
which tries to identify the version from the server.

There are 1 of 2 ways to get an API:

- Using the convenience methods in ARI (action)
```java
ActionApplications ac = ari.applications();
```
- or asking ARI to get an (action) implementation
```java
ActionApplications ac = ari.getActionImpl(ActionApplications.class);
```

There are also 1 of 2 ways for invoking an operation:

- Using synchronous execution
```java
AsteriskInfo info = ari.asterisk().getInfo().execute();
```

- or asynchronous execution
```java
AsteriskInfo info = ari.asterisk().getInfo().execute(new AriCallback<AsteriskInfo>() {
    @Override
    public void onSuccess(AsteriskInfo result) {
        // handle result
    }
    @Override
    public void onFailure(RestException e) {
        e.printStackTrace();
    }
});
```

An ARI application is pretty much useless unless you subscribe to and handle the events,
so you need to create the websocket and choose to subscribe to all events or only events for actions made by your app.
This must use the asynchronous execution as the events are asynchronous.
```java
AriCallback<Message> callback = new AriCallback<Message>() {
    @Override
    public void onSuccess(Message message) {
        // code to handle the events ...
    }
    @Override
    public void onFailure(RestException e) {
        e.printStackTrace();
    }
};
ari.events().eventWebsocket("app-name").execute(callback);
// or subscribe to all events
// ari.events().eventWebsocket("app-name").setSubscribeAll(true).execute(callback);
```
The Message object will be one of many subtypes, you'll have to introspect to find out which. 
```java
public void onSuccess(Message message) {
    if (message instanceof StasisStart) {
        // handle the start (from dialplan or channels.originate API)
    } else if (message instanceof StasisEnd) {
        // handle the end - good bye
    }
}
```

Most likely ARI applications are going to be long running applications, 
so the above will be a common, however for convenience you can get a `MessageQueue` from ARI.
Which utilises a polling strategy which is not great in this event based world...
```java
MessageQueue queue = ari.getWebsocketQueue();
// perform some actions...
Message message = queue.dequeueMax(10, 250);
if (message != null) {
    // handle event
}
``` 

When you're done or you're quitting the application make sure to cleanup.
```java
ARI ari = ARI.build(...);
try {
    // your logic
} finally {
    ari.cleanup();
}
```

If you want to be more multi-threaded it would be a good idea to implement a thread pool
and handle each event in a separate thread. 
[Here](https://github.com/l3nz/ari4java/blob/master/src/test/java/ch/loway/oss/ari4java/sandbox/sample/Weasels.java)
is example with a fixed pool of 10 threads that handle each event. The thread will play the 'weasels-eaten-phonesys'
sound on when someone dials in and will hangup when playback is complete.
(assuming the dial plan executes `Stasis` and you have that sound)

Dial plan (`/etc/asterisk/extensions.conf`):
```
[default]
exten => _X.,1,NoOp(Incoming to ${EXTEN} send to Stasis})
same => Answer()
same => Stasis(weasels-app)
```
