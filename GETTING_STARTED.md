Getting started with ari4java (and ARI in general) is not that easy. This is because there are many moving parts involved:

* An Asterisk server
* A specific configuration for Asterisk
* ari4java itself

In order to reduce the cost of getting up and running, we thought of providing a pre-configured Docker image that runs Asterisk,
and an example that you can run yourself though an IDE.

## Getting the Docker image

This is quite easy - first you will have to install Docker; when it's up, you can then do a:

     docker run -p 18088:8088 -P -d lenz/asterisk-load-test-13

to run a Docker image with Asterisk 13 and bind its ARI port to local port 18088.


## Running your first ari4java program

We have a very simple stub that:

* Creates a a bridge
* Originates a call
* Monitor events for 20 seconds
* Destroys the bridge

and that shows what it is doing on stdout. In order to run it, you just have to edit the credentials in ConnectAndDial.java.




